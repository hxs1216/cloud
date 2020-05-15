package com.biyao.service.impl;

import com.biyao.config.properties.MongoProperties;
import com.biyao.constants.Constants;
import com.biyao.entities.DataSourceInfo;
import com.biyao.multi.mongo.TenantMongoDatabaseProvider;
import com.biyao.service.InitTenantService;
import com.biyao.service.TenantDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


@Service
@Order(0)
public class SimpleInitTenantServiceImpl implements InitTenantService {

    private static final Logger log = LoggerFactory.getLogger(SimpleInitTenantServiceImpl.class);

    @Autowired
    private TenantDataSourceService tenantDataSourceService;

    @Autowired(required = false)
    private MongoProperties mongoProperties;

    @Override
    public boolean initTenantInfo(DataSourceInfo dataSourceInfo) {
        log.info("simple init tenantInfo:{}", dataSourceInfo);
        // 使用springLiquibase初始化数据库
        if (Constants.PROFILE_MYSQL.equalsIgnoreCase(dataSourceInfo.getType())) {
            tenantDataSourceService.initDatabase(dataSourceInfo);
            tenantDataSourceService.addRemoteDataSource(dataSourceInfo);
        } else if (Constants.PROFILE_MONGO.equalsIgnoreCase(dataSourceInfo.getType()) && null != mongoProperties) {
            // mongo初始化
            TenantMongoDatabaseProvider.setDatabase(dataSourceInfo.getDatabase(), dataSourceInfo.getDatabase());
        }
        return true;
    }
}