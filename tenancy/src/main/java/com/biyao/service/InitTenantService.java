package com.biyao.service;


import com.biyao.entities.DataSourceInfo;

/**
 * 功能描述 初始化租户数据库
 *
 * @author hxs
 * @date 2020/5/15
 */
public interface InitTenantService {

    /**
     * 初始化租户信息
     */
    boolean initTenantInfo(DataSourceInfo dataSourceInfo);

}