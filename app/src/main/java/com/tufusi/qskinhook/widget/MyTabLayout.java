package com.tufusi.qskinhook.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;
import com.tufusi.qskin.SkinViewSupport;
import com.tufusi.qskin.utils.SkinResources;
import com.tufusi.qskinhook.R;

/**
 * Created by 鼠夏目 on 2020/8/19.
 *
 * @author 鼠夏目
 * @description
 */
public class MyTabLayout extends TabLayout implements SkinViewSupport {

    private int tabTextColorResId;
    private int tabIndicatorColorResId;

    public MyTabLayout(Context context) {
        this(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyleAttr, 0);
        tabIndicatorColorResId = ta.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0);
        tabTextColorResId = ta.getResourceId(R.styleable.TabLayout_tabTextColor, 0);

        ta.recycle();
    }

    @Override
    public void applySkin() {
        if (tabIndicatorColorResId != 0) {
            int tabIndicatorColor = SkinResources.getInstance().getColor(tabIndicatorColorResId);
            setSelectedTabIndicatorColor(tabIndicatorColor);
        }

        if (tabTextColorResId != 0) {
            ColorStateList tabITextColor = SkinResources.getInstance().getColorStateList(tabTextColorResId);
            setTabTextColors(tabITextColor);
        }
    }
}