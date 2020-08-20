package com.tufusi.qskinhook;

import android.app.Application;

import com.tufusi.qskin.SkinManager;

/**
 * Created by 鼠夏目 on 2020/8/19.
 *
 * @author 鼠夏目
 * @description
 */
public class Core extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化换肤插件
        SkinManager.init(this);
    }
}