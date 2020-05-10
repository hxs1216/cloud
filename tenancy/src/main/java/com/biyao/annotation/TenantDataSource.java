package com.biyao.annotation;


import java.lang.annotation.*;

/**
 * 功能描述 动态数据源注解
 *
 * @author hxs
 * @date 2020/5/10
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantDataSource {

    /**
     * 租户名称 默认为空表示所有数据源都会遍历访问
     */
    String[] value() default {};

    /**
     * 默认异步执行
     */
    boolean async() default true;
}