package com.tufusi.qskin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;

/**
 * Created by 鼠夏目 on 2020/8/20.
 *
 * @author 鼠夏目
 * @description 皮肤资源管理类
 */
public class SkinResources {

    private static volatile SkinResources singleton = null;

    /**
     * app未换肤前的资源类
     */
    private Resources mAppResources;

    /**
     * 皮肤包的新资源类
     */
    private Resources mSkinResources;

    /**
     * 是否是默认皮肤 默认是
     */
    private boolean isDefaultSkin = true;

    /**
     * 皮肤插件包包名
     */
    private String mSkinPkgName;

    private SkinResources(Context context) {
        mAppResources = context.getResources();
    }

    public static SkinResources init(Context context) {
        if (singleton == null) {
            synchronized (SkinResources.class) {
                if (singleton == null) {
                    singleton = new SkinResources(context);
                }
            }
        }
        return singleton;
    }

    public static SkinResources getInstance() {
        return singleton;
    }

    /**
     * 应用插件皮肤包
     */
    public void applySkinApk(Resources resources, String pkgName) {
        mSkinPkgName = pkgName;
        mSkinResources = resources;

        // 是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 根据主APP的资源类ID， 到皮肤apk文件中取搜寻对应的ID的颜色值
     */
    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }

        int skinId = getIdentifier(resId);
        if (skinId == 0){
            return mAppResources.getColor(resId);
        }

        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin){
            return mAppResources.getColorStateList(resId);
        }

        int skinId = getIdentifier(resId);
        if (skinId == 0){
            return mAppResources.getColorStateList(resId);
        }

        return mSkinResources.getColorStateList(skinId);
    }

    /**
     * 1.通过原始的APP 中的 resId(R.color.XXXX) 找出自己的名字
     * 2.根据名字和类型获取皮肤包中的指定资源名称的资源标识符
     */
    private int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }
}