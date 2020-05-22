package com.biyao.security.client;


import com.biyao.security.dto.BackendAuthorityDTO;
import com.biyao.security.dto.RoleDataAuthBindDTO;

import java.util.List;

/**
 * 功能描述 oauth2 权限相关接口
 *
 * @author hxs
 * @date 2020/5/20
 */
public interface OAuth2AuthorityEndpointClient {

    /**
     * 获取后端权限对应的用户id集合
     */
    List<BackendAuthorityDTO> getAuthorityUserIdsMap();

    /**
     * 获取用户所有数据权限
     */
    List<RoleDataAuthBindDTO> getUserDataAuthorityList(Long userId);

    /**
     * 获取用户所在部门
     */
    List<Long> getUserDepartmentIdList(Long userId);
}
