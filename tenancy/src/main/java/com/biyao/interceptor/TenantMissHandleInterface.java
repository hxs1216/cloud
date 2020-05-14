package com.biyao.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于请求缺失租户信息的处理
 */
public interface TenantMissHandleInterface {

    /**
     * 是否需要处理
     */
    boolean match(HttpServletRequest request);

    /**
     * 生成租户id
     */
    String genTenantCode(HttpServletRequest request);
}
