package com.tufusi.zeropermission.aop;

import android.content.Context;
import android.util.Log;

import com.tufusi.zeropermission.PermissionRequestActivity;
import com.tufusi.zeropermission.annotation.PermissionCancel;
import com.tufusi.zeropermission.annotation.PermissionRequired;
import com.tufusi.zeropermission.util.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by 鼠夏目 on 2020/7/21.
 *
 * @author 鼠夏目
 * @description 提取切面类
 * @see
 */
@Aspect
public class PermissionAspect {

    /**
     * 注解切点：
     * 定义一个方法，用于声明切入点表达式。一般的，该方法中不再需要添加其他代码。
     * 使用@Pointcut 来声明切入点表达式
     * 后面的其他通知直接使用方法名来引用当前的切入点表达式
     */
    @Pointcut("execution(@com.tufusi.zeropermission.annotation.PermissionRequired * *(..)) && @annotation(required)")
    public void requestPermission(PermissionRequired required) {

    }

    /**
     * 环绕增强：用来注解在调用一个具体方法前和具体方法后来完成一些具体的任务
     * JoinPoint: 表示目标类连接点对象
     * 此处用环绕增强，因此使用ProceedingJoinPoint
     *
     * @param joinPoint JoinPoint子类接口  表示连接点对象
     * @param required
     */
    @Around("requestPermission(required)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, PermissionRequired required) {
        //获取连接对象 即Activity
        final Object obj = joinPoint.getThis();
        //获取上下文环境
        Context context = (Context) obj;

        //实现动态权限申请
        PermissionRequestActivity.startPermissionRequest(context, required.requestPermission(),
                required.requestCode(), new IPermission() {
                    @Override
                    public void onPermissionGranted() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied(int requestCode) {
                        PermissionUtil.invokeAnnotation(obj, PermissionRequired.class, requestCode);
                    }

                    @Override
                    public void onPermissionCanceled(int requestCode) {
                        PermissionUtil.invokeAnnotation(obj, PermissionCancel.class, requestCode);
                    }
                });
    }
}
