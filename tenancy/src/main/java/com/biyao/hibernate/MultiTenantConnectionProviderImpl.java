package com.biyao.hibernate;


import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;

/**
 * 多租户数据源提供者
 * 这个类是Hibernate框架拦截sql语句并在执行sql语句之前更换数据源提供的类
 *
 * @Author tanglh
 * @Date 2018/11/23 12:47
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    /**
     * 默认数据源
     *
     * @return
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return selectDataSource(TenantDataSourceProvider.DEFAULT_KEY);
    }

    /**
     * 自定义数据源
     * 不存在则返回默认
     *
     * @param tenantIdentifier
     * @return
     */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return TenantDataSourceProvider.getTenantDataSource(tenantIdentifier);
    }

}
