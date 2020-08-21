package com.tufusi.zeropermission;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tufusi.zeropermission.aop.IPermission;
import com.tufusi.zeropermission.util.PermissionUtil;

/**
 * 启动权限请求的Activity，这个activity是启动在另一个task里面，并且没有布局
 * 为的是实现一个承载请求权限的容器，而用户却感知不到（透明主题）
 */
public class PermissionRequestActivity extends Activity {

    private static final String REQUEST_PERMISSION = "request_permission";
    private static final String REQUEST_CODE = "request_code";
    private static IPermission mIPermission;

    public static void startPermissionRequest(Context context, String[] permissions,
                                              int requestCode, IPermission iPermission) {
        mIPermission = iPermission;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(REQUEST_PERMISSION, permissions);
        bundle.putInt(REQUEST_CODE, requestCode);
        intent.putExtras(bundle);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String[] permissions = bundle.getStringArray(REQUEST_PERMISSION);
        int requestCode = bundle.getInt(REQUEST_CODE);

        requestPermission(permissions, requestCode);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        if (PermissionUtil.hasSelfPermission(this, permissions)) {
            //权限已经有了
            mIPermission.onPermissionGranted();
            finish();
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.verifyPermissions(grantResults)) {
            //权限已经有了
            mIPermission.onPermissionGranted();
        } else {
            //判断权限是否需要给出提示
            if (PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
                //取消权限
                mIPermission.onPermissionCanceled(requestCode);
            } else {
                //拒绝权限
                mIPermission.onPermissionDenied(requestCode);
            }
        }
        finish();
        overridePendingTransition(0, 0);
    }
}
