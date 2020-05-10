package com.biyao.annotation;
import java.lang.annotation.*;


/**
 * 功能描述 注解仅用于 TenantMissHandleInterface
 * 实现类标识是否使用 MyHttpServletRequestWrapper 包装请求
 *
 * @author hxs
 * @date 2020/5/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrapperRequest {
}
