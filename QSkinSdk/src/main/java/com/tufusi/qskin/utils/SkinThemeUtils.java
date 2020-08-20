package com.tufusi.qskin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

/**
 * Created by 鼠夏目 on 2020/8/20.
 *
 * @author 鼠夏目
 * @description 皮肤主题管理类
 */
public class SkinThemeUtils {

    private static final int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};
    private static final int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {androidx.appcompat.R.attr.colorPrimaryDark};

    public static void updateStatusBarColor(Activity activity) {
        // 5.0以上才能修改状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        // 获得 statusBarColor 和 navigationBarColor (状态栏颜色）
        // 当与 colorPrimaryDark 不一致时，以 statusBarColor 为准
        int[] resIds = getResId(activity, STATUSBAR_COLOR_ATTRS);
        int statusBarColorResId = resIds[0];
        int navigationBarColorResId = resIds[1];

        // 如果直接在style中写入固定颜色值（而不是@color/XXXX） 则为0
        if (statusBarColorResId == 0) {
            // 获得 colorPrimaryDark
            int colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if (colorPrimaryDarkResId != 0) {
                int color = SkinResources.getInstance().getColor(colorPrimaryDarkResId);
                activity.getWindow().setStatusBarColor(color);
            }
        } else {
            int color = SkinResources.getInstance().getColor(statusBarColorResId);
            activity.getWindow().setStatusBarColor(color);
        }

        if (navigationBarColorResId != 0) {
            int color = SkinResources.getInstance().getColor(navigationBarColorResId);
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 获得Theme中属性中定义的 资源ID
     *
     * @param context
     * @param attrs
     */
    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray ta = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < attrs.length; i++) {
            resIds[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        return resIds;
    }
}