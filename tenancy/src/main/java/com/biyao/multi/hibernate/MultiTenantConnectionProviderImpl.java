package com.biyao.multi.hibernate;


import com.biyao.multi.TenantDataSourceProvider;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

/**
 * 功能描述 多租户数据源提供者
 * 这个类是Hibernate框架拦截sql语句并在执行sql语句之前更换数据源提供的类
 *
 * @author hxs
 * @date 2020/5/15
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    /**
     * 默认数据源
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return selectDataSource(TenantDataSourceProvider.DEFAULT_KEY);
    }

    /**
     * 自定义数据源
     * 不存在则返回默认
     */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return TenantDataSourceProvider.getTenantDataSource(tenantIdentifier);
    }

}
