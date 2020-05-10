package com.biyao.multi;

import com.biyao.util.ThreadTenantUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 功能描述 动态数据源
 * Spring boot提供了AbstractRoutingDataSource 根据用户定义的规则选择当前的数据源，
 * 这样我们可以在执行查询之前，设置使用的数据源。实现可动态路由的数据源，
 * 在每次数据库查询操作前执行。它的抽象方法 determineCurrentLookupKey() 决定使用哪个数据源。
 *
 * @author hxs
 * @date 2020/5/10
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static DynamicDataSource instance;

    private DynamicDataSource() {
    }

    /**
     * 获取实例
     */
    public static synchronized DynamicDataSource getInstance() {
        if (null == instance) {
            instance = new DynamicDataSource();
        }
        return instance;
    }

    /**
     * 设置数据源
     */
    public void targetDataSources(Map<String, DataSource> dataSourceMap) {
        if (null == dataSourceMap) {
            return;
        }
        Map<Object, Object> targetDataSources = new HashMap<>(16);
        dataSourceMap.forEach(targetDataSources::put);
        this.setTargetDataSources(targetDataSources);

    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return Optional.ofNullable(ThreadTenantUtil.getTenant()).orElse(TenantDataSourceProvider.DEFAULT_KEY);
    }
}