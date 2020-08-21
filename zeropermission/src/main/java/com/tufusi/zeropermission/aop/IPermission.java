package com.tufusi.zeropermission.aop;

/**
 * Created by 鼠夏目 on 2020/7/21.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public interface IPermission {

    //同意权限
    void onPermissionGranted();

    //拒绝权限并且选中不再提示
    void onPermissionDenied(int requestCode);

    //取消权限
    void onPermissionCanceled(int requestCode);
}
