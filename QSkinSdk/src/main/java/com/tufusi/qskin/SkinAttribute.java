package com.tufusi.qskin;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.tufusi.qskin.utils.SkinResources;
import com.tufusi.qskin.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鼠夏目 on 2020/8/20.
 *
 * @author 鼠夏目
 * @description 该类管理所有要换肤的View对应的属性
 */
public class SkinAttribute {

    private static List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    // 记录换肤需要操作的View 与属性信息
    private List<SkinView> mSkinViews = new ArrayList<>();

    /**
     * 对所有的View中的所有属性进行皮肤修改
     */
    public void applySkin() {
        for (SkinView skinView : mSkinViews) {
            skinView.applySkin();
        }
    }

    /**
     * 记录下一个 View 身上有哪几个属性需要换肤 textColor/src
     */
    public void look(View view, AttributeSet attributeSet) {
        List<SkinPair> mSkinPairs = new ArrayList<>();

        for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
            //获得属性名 textColor/background
            String attributeName = attributeSet.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                // #
                // ?722727272
                // @722727272
                String attributeValue = attributeSet.getAttributeValue(i);
                // 如果是以 # 开头写死的颜色，则不可用于换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }

                int resId;
                // 以 ？开头的表示使用的属性
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    // 正常以 @ 开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }

                SkinPair skinPair = new SkinPair(attributeName, resId);
                mSkinPairs.add(skinPair);
            }
        }

        if (!mSkinPairs.isEmpty() || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, mSkinPairs);
            //如果选择过皮肤，调用一次 applySkin 加载皮肤的资源
            skinView.applySkin();
            mSkinViews.add(skinView);
        }
    }

    static class SkinView {

        View view;
        /**
         * View 可以参与换肤的所有属性的 ID 的集合
         */
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        /**
         * 对View中的所有属性均遍历修改
         */
        public void applySkin() {
            applySkinSupport();

            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;

                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        //获取到的可能是 Color, 也可能是 Drawable
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }

                if (left != null || right != null || top != null || bottom != null) {
                    ((TextView) view).setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
                }
            }
        }

        private void applySkinSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
    }

    static class SkinPair {
        // 属性名
        String attributeName;
        // 对应资源的ID
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}