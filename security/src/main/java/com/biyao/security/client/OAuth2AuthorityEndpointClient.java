package com.biyao.security.client;

import com.taoqi.security.dto.BackendAuthorityDTO;
import com.taoqi.security.dto.RoleDataAuthBindDTO;

import java.util.List;

/**
 * @author tyjuncai
 * @ClassName: OAuth2AuthorityEndpointClient
 * @Description: oauth2 权限相关接口
 * @date 2018/12/20 10:58
 * @see UaaAuthorityEndpointClient
 * @see OAuth2AuthorityEndpointClientAdapter
 */
public interface OAuth2AuthorityEndpointClient {

	/**
	 * 获取后端权限对应的用户id集合
	 * @return
	 */
	List<BackendAuthorityDTO> getAuthorityUserIdsMap();

	/**
	 * 获取用户所有数据权限
	 * @param userId
	 * @return
	 */
	List<RoleDataAuthBindDTO> getUserDataAuthorityList(Long userId);

	/**
	 * 获取用户所在部门
	 * @param userId
	 * @return
	 */
	List<Long> getUserDepartmentIdList(Long userId);
}
