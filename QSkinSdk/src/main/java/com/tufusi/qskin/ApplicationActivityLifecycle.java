package com.tufusi.qskin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        try {
            // 如果布局加载器已经加载过  则此标记mFactorySet会被标注为true，导致我们自定义的Factory2会失效，
            // 因此通过反射修改此标记，使得我们的 Factory2 可以工作
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SkinLayoutInflaterFactory inflaterFactory = new SkinLayoutInflaterFactory(activity);
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

    }
}