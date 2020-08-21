package com.tufusi.zeropermission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 鼠夏目 on 2020/7/21.
 *
 * @See
 * @Description 注解权限必要
 * 注解的目标是方法
 * 注解保留期是运行期
 * <p>
 * 这里简单介绍下：
 * SOURCE：保留在源文件（即使用该注释会在进入编译器编译之前把这个注释丢弃--一般用于语法校验等等）
 * CLASS：保留到类文件（编译器将注释记录在类文件中，但是VM在运行时不需要保留注释，这是默认的策略）
 * RUNTIME：保留到运行期（注解不仅被保存到类文件中，同时VM加载类文件之后，注释仍然存在） -- 这里需要到具体业务中涉及操作 就声明运行期
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequired {

    /**
     * 请求权限
     */
    String[] requestPermission();

    /**
     * 请求码
     */
    int requestCode();
}
