package com.biyao.security.provider;

import com.biyao.security.constant.DataAuthDepartmentType;
import com.biyao.security.constant.DataAuthItemType;
import com.biyao.security.dto.RoleDataAuthBindDTO;
import com.biyao.security.dto.RoleDataAuthDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述 Spring Security上下文用户信息操作工具类
 *
 * @author hxs
 * @date 2020/5/21
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前登录的用户信息
     *
     */
    public static Optional<SecurityUser> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof SecurityUser) {
                        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                        return securityUser;
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    /**
     * 校验当前用户是否有某角色
     *
     * @param role
     * @return
     */
    public static boolean isCurrentUserInRole(String role) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role)))
                .orElse(false);
    }

    /**
     * 获取当前登录用户所属业务方标识
     *
     * @return
     */
    public static Optional<String> getCurrentUserTenantCode() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof SecurityUser) {
                        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                        return securityUser.getTenantCode();
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public static Optional<Long> getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof SecurityUser) {
                        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                        return securityUser.getUserId();
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户的角色
     *
     * @return
     */
    public static Optional<Collection<GrantedAuthority>> getCurrentUserAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication instanceof UserAuthenticationToken) {
                        UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authentication;
                        Collection<GrantedAuthority> authorities = userAuthenticationToken.getAuthorities();
                        return authorities;
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户的数据权限
     *
     * @return
     */
    public static Optional<SecurityAuthority> getCurrentUserAuthority() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication instanceof UserAuthenticationToken) {
                        UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authentication;
                        return userAuthenticationToken.getSecurityAuthority();
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户指定数据权限类型的数据权限，并合并数据权限项
     *
     * @param dataAuthTypeId
     * @param roleType
     * @return
     */
    public static Optional<List<String>> getCurrentUserDataAuthList(Long dataAuthTypeId, Integer roleType) {
        Optional<SecurityAuthority> securityAuthorityOptional = getCurrentUserAuthority();
        return securityAuthorityOptional.map(securityAuthority -> {
            List<String> result = new ArrayList<>();
            if (dataAuthTypeId == null) {
                return result;
            }
            if (securityAuthority != null && CollectionUtils.isNotEmpty(securityAuthority.getDataAuthorityList())) {
                List<RoleDataAuthBindDTO> dataAuthorityList = securityAuthority.getDataAuthorityList();
                if (roleType != null) {
                    dataAuthorityList = dataAuthorityList.stream().filter(roleDataAuthBindDTO -> ObjectUtils.equals(roleDataAuthBindDTO.getRoleType(),
                            roleType)).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(dataAuthorityList)) {
                    List<RoleDataAuthDTO> roleDataAuthList = new ArrayList<>();
                    dataAuthorityList.forEach(roleDataAuthBindDTO -> {
                        if (CollectionUtils.isNotEmpty(roleDataAuthBindDTO.getRoleDataAuthList())) {
                            roleDataAuthList.addAll(roleDataAuthBindDTO.getRoleDataAuthList());
                        }
                    });
                    List<RoleDataAuthDTO> finalRoleDataAuthList = roleDataAuthList.stream().filter(roleDataAuthDTO -> ObjectUtils.equals
                            (roleDataAuthDTO.getDataAuthTypeId(), dataAuthTypeId)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(finalRoleDataAuthList)) {
                        finalRoleDataAuthList.forEach(roleDataAuthDTO -> {
                            if (CollectionUtils.isNotEmpty(roleDataAuthDTO.getDataList())) {
                                List<String> dataList = roleDataAuthDTO.getDataList();
                                if (StringUtils.equals(roleDataAuthDTO.getDataType(), String.valueOf(DataAuthItemType.DEPARTMENT.getValue()))) {
                                    if (dataList.contains(DataAuthDepartmentType.OWN.getValue())) {
                                        Optional<SecurityUser> securityUser = getCurrentUser();
                                        securityUser.ifPresent(user -> {
                                            if (CollectionUtils.isNotEmpty(user.getDepartmentIds())) {
                                                List<String> departmentIds = user.getDepartmentIds().stream().map(String::valueOf).collect(Collectors.toList());
                                                dataList.addAll(departmentIds);
                                            }
                                        });
                                        dataList.remove(DataAuthDepartmentType.OWN.getValue());
                                    } else {
                                        dataList.remove(DataAuthDepartmentType.DEFINE.getValue());
                                    }
                                }
                                result.addAll(dataList);
                            }
                        });
                    }
                }
            }
            return result;
        });
    }

    /**
     * 获取当前登录用户的角色列表
     *
     * @return
     */
    public static Optional<Set<Long>> getCurrentRoleIds() {
        Optional<SecurityAuthority> optional = getCurrentUserAuthority();
        return optional.map(o -> o.getDataAuthorityList().stream().map(RoleDataAuthBindDTO::getRoleId).collect(Collectors.toSet()));
    }
}
