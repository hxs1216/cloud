package com.biyao.entities;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

import java.io.Serializable;

public class TenantCacheKey implements Serializable {
    private final String tenantCode;
    private final SimpleKey simpleKey;

    public TenantCacheKey(String tenantCode, Object... params) {
        Assert.notNull(params, "params is null");
        this.tenantCode = tenantCode;
        this.simpleKey = new SimpleKey(params);
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public SimpleKey getSimpleKey() {
        return simpleKey;
    }
}