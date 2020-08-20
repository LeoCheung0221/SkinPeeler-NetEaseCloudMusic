package com.tufusi.qskin;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tufusi.qskin.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 鼠夏目 on 2020/8/19.
 *
 * @author 鼠夏目
 * @description 自定义皮肤布局加载器工厂  用来接管系统的View生产过程
 * 同时也是一个观察者 观察是否应用换肤
 */
public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {

    private static final String[] mClassPrefixList = {
            "android.widget",
            "android.webkit",
            "android.app",
            "android.view"
    };

    /**
     * 记录对应View的构造函数
     */
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class
    };

    private static final HashMap<String, Constructor<? extends View>> mConstructorMap = new HashMap<>();

    /**
     * 页面属性管理器：当选择新皮肤后需要替换View与之对应的属性
     */
    private SkinAttribute skinAttribute;

    /**
     * 用于获取窗口的状态框的信息
     */
    private Activity mActivity;

    public SkinLayoutInflaterFactory(Activity activity) {
        mActivity = activity;
        skinAttribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        // 此处开始创建View 并进行View属性修改
        View view = createSDKView(name, context, attributeSet);

        if (view == null) {
            view = createOriginView(name, context, attributeSet);
        }
        if (view != null) {
            // 加载属性
            skinAttribute.look(view, attributeSet);
        }

        return view;
    }

    private View createOriginView(String name, Context context, AttributeSet attributeSet) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        if (constructor == null) {
            return null;
        }
        try {
            return constructor.newInstance(context, attributeSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private View createSDKView(String name, Context context, AttributeSet attributeSet) {
        // 如果包含 则不是SDK中的 view 可能是自定义 view 或者包括support库中的view
        if (name.indexOf(".") != -1) {
            return null;
        }

        // 不包含就需要在解析的 节点 name前，拼接: android.widget. 等等去尝试反射
        for (int i = 0; i < mClassPrefixList.length; i++) {
            View view = createOriginView(mClassPrefixList[i] + name, context, attributeSet);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }

    /**
     * 如果有接收到通知，这里就会被执行
     */
    @Override
    public void update(Observable observable, Object o) {
        SkinThemeUtils.updateStatusBarColor(mActivity);
        skinAttribute.applySkin();
    }
}