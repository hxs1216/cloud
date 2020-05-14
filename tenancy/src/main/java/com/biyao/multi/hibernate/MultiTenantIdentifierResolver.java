package com.biyao.multi.hibernate;

import com.biyao.util.ThreadTenantUtil;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import java.util.Optional;

/**
 * 多租户解析器
 *
 * @author: hxs
 * @create: 2020/4/26
 */
public class MultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    // 获取tenantId的逻辑在这个方法里面写
    @Override
    public String resolveCurrentTenantIdentifier() {
        return Optional.ofNullable(ThreadTenantUtil.getTenant()).orElse(TenantDataSourceProvider.DEFAULT_KEY);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}