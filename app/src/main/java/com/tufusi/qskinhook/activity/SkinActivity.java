package com.tufusi.qskinhook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tufusi.qskin.SkinManager;
import com.tufusi.qskinhook.R;
import com.tufusi.zeropermission.annotation.PermissionCancel;
import com.tufusi.zeropermission.annotation.PermissionDenied;
import com.tufusi.zeropermission.annotation.PermissionRequired;

public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void change(View view) {
        changeSkin();
    }

    @PermissionRequired(requestPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode = 12)
    private void changeSkin() {
        Log.e("tufusi", "changeSkin: 请求权限成功");
        SkinManager.getInstance().loadSkinBag("/data/data/com.tufusi.qskinhook/SkinBag-debug.apk");
    }

    @PermissionCancel()
    public void permissionCancel(int requestCode) {
        Log.e("tufusi", "changeSkin: 请求权限取消" + requestCode);
    }

    @PermissionDenied()
    public void permissionDenied(int requestCode) {
        Log.e("tufusi", "changeSkin: 请求权限拒绝" + requestCode);
    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkinBag(null);
    }
}