package com.tufusi.qskin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.tufusi.qskin.utils.SkinPreference;
import com.tufusi.qskin.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by 鼠夏目 on 2020/8/19.
 *
 * @author 鼠夏目
 * @description 皮肤包管理类 被观察对象
 */
public class SkinManager extends Observable {

    private volatile static SkinManager sInstance;
    private Application mContext;
    private ApplicationActivityLifecycle skinActivityLifecycle;

    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (SkinManager.class) {
                if (sInstance == null) {
                    sInstance = new SkinManager(application);
                }
            }
        }
    }

    private SkinManager(Application application) {
        mContext = application;
        //共享首选项  用于记录当前使用的皮肤
        SkinPreference.init(application);
        //资源管理类，用于获取从 app/皮肤插件 中资源加载
        SkinResources.init(application);
        // 注册Activity生命周期，并设置被观察者
        skinActivityLifecycle = new ApplicationActivityLifecycle(this);
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle);
        // 记载上次保存的皮肤包
        loadSkinBag(SkinPreference.getInstance().getSkin());
    }

    public static SkinManager getInstance() {
        return sInstance;
    }

    /**
     * 加载皮肤插件包 并应用皮肤资源
     *
     * @param apkPath 插件包文件路径
     */
    public void loadSkinBag(String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            // 还原默认皮肤
            SkinPreference.getInstance().reset();
            SkinResources.getInstance().reset();
        } else {
            try {
                // 通过反射获取AssetManager
                AssetManager assetManager = AssetManager.class.newInstance();
                // 资源路径设置
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, apkPath);

                // 宿主APP 的resources
                Resources appResources = mContext.getResources();
                // 根据当前显示器设备信息，与配置（横竖屏、lang）创建Resources
                Resources skinResources = new Resources(assetManager, appResources.getDisplayMetrics(),
                        appResources.getConfiguration());
                // 获取皮肤包 包名
                PackageManager pm = mContext.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
                String pkgName = info.packageName;

                SkinResources.getInstance().applySkinApk(skinResources, pkgName);

                // 存储记录
                SkinPreference.getInstance().setSkin(apkPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 通知所有采用的View 更新皮肤
        // 被观察者变动 通知所有观察者
        setChanged();
        notifyObservers(null);
    }
}
