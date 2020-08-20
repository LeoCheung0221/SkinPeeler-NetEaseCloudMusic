package com.tufusi.qskin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 鼠夏目 on 2020/8/19.
 *
 * @author 鼠夏目
 * @description 自定义皮肤布局加载器工厂  用来接管系统的View生产过程
 * 同时也是一个观察者 观察是否应用换肤
 */
public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2 {

    @Nullable
    @Override
    public View onCreateView(@Nullable View view, @NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }
}