package com.tufusi.zeropermission.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;

import com.tufusi.zeropermission.PermissionRequestActivity;
import com.tufusi.zeropermission.annotation.PermissionRequired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 鼠夏目 on 2020/7/21.
 *
 * @author 鼠夏目
 * @description 权限处理工具类 这里留存着各个最低SDK版本对应的权限
 * @see
 */
public class PermissionUtil {

    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    /**
     * 判断所有权限是否都同意了，都同意返回true，否则返回false
     *
     * @param context     上下文环境
     * @param permissions 权限列表
     * @return 返回true 如果所有的权限都同意
     */
    public static boolean hasSelfPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断权限是否存在
     *
     * @return return true if permission exists in SDK version
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    /**
     * 判断单个权限是否授予同意
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查是否都赋予权限
     *
     * @param grantResults 赋予结果集
     * @return 所有都同意返回true， 否则都返回false
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查所给权限数组是否需要给出提示
     *
     * @param activity    上下文环境
     * @param permissions 权限列表
     * @return 返回true 如果某个权限需要给出提示
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过反射调用指定方法
     *
     * @param obj             切面上下文环境
     * @param annotationClass 注解类
     * @param requestCode     请求码
     */
    public static void invokeAnnotation(Object obj, Class annotationClass, int requestCode) {
        //获取切面的上下文的类型
        Class<?> clazz = obj.getClass();
        //获取类型中的方法
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) {
            return;
        }

        for (Method method : methods) {
            //获取该方法是否有 annotationClass 注解
            boolean isHasAnnotation = method.isAnnotationPresent(annotationClass);
            if (isHasAnnotation) {
                //判断是否有且仅有一个int 参数
                Class<?>[] parameterType = method.getParameterTypes();
                if (parameterType.length != 1) {
                    throw new RuntimeException("有且仅有一个 int 参数");
                }

                method.setAccessible(true);
                try {
                    method.invoke(obj, requestCode);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
