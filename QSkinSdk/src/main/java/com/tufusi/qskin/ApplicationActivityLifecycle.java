package com.tufusi.qskin;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.tufusi.qskin.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * Created by 鼠夏目 on 2020/8/20.
 *
 * @author 鼠夏目
 * @description
 */
public class ApplicationActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Observable mObservable;

    private ArrayMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactories = new ArrayMap<>();

    public ApplicationActivityLifecycle(Observable observable) {
        mObservable = observable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        /**
         * 更新状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity);

        /**
         * 更新布局视图
         */
        //获得布局加载器
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        // 使用Factory2设置布局加载过程
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                // 如果布局加载器已经加载过  则此标记mFactorySet会被标注为true，导致我们自定义的Factory2会失效，
                // 因此通过反射修改此标记，使得我们的 Factory2 可以工作
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(layoutInflater, false);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ApplicationLifecycle", e.toString());
            }
            LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        } else {
            forceSetFactory2(layoutInflater, skinLayoutInflaterFactory);
        }
        mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);

        // 添加观察者，一个被观察者
        // 这个被观察者是 SkinManager
        mObservable.addObserver(skinLayoutInflaterFactory);
    }

    /**
     * 自定义 LayoutInflaterCompat 的 forceSetFactory2 方法
     * LayoutInflaterCompat 针对Factory2 的做法，即直接修改 mFactory2 的值
     *
     * @param layoutInflater 布局加载器
     * @param factory        工厂接口类
     */
    private static void forceSetFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory) {
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;

        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(layoutInflater, false);

            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
            mFactory2.set(layoutInflater, factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }
}