package com.biyao.security.filter;

import com.biyao.security.config.SecurityProperties;
import com.biyao.security.constant.CtrlType;
import com.biyao.security.constant.UserType;
import com.biyao.security.dto.RoleDataAuthBindDTO;
import com.biyao.security.provider.SecurityAuthority;
import com.biyao.security.provider.SecurityUser;
import com.biyao.security.provider.SecurityUtils;
import com.biyao.security.provider.UserAuthenticationToken;
import com.biyao.security.service.OAuth2AuthorityService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 功能描述 获取权限过滤器
 *
 * @author hxs
 * @date 2020/5/22
 */
public class LoadAuthorityFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(ValidAuthorityFilter.class);

    private final OAuth2AuthorityService authorityService;

    private final SecurityProperties securityProperties;

    public LoadAuthorityFilter(OAuth2AuthorityService authorityService, SecurityProperties securityProperties) {
        this.authorityService = authorityService;
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            // 获取当前登录用户
            Optional<SecurityUser> securityUserOp = SecurityUtils.getCurrentUser();
            if (securityUserOp.isPresent()) {
                SecurityUser securityUser = securityUserOp.get();
                // 用户权限对象
                SecurityAuthority securityAuthority = new SecurityAuthority();
                if (ObjectUtils.equals(securityUser.getType(), UserType.business.getValue())) {
                    // 获取用户所在部门
                    List<Long> departmentIdList = authorityService.getUserDepartmentIdList(securityUser.getUserId());
                    if (CollectionUtils.isNotEmpty(departmentIdList)) {
                        securityUser.setDepartmentIds(departmentIdList);
                    }
                    // 获取用户角色和数据权限
                    List<RoleDataAuthBindDTO> dataAuthorityDTOList = authorityService.getUserDataAuthorityList(securityUser.getUserId());
                    if (CollectionUtils.isNotEmpty(dataAuthorityDTOList)) {
                        // 判读是否存在不受权限控制的角色
                        List<RoleDataAuthBindDTO> notCtrlList = dataAuthorityDTOList.stream().filter(item -> ObjectUtils.equals(item.getCtrlType(), CtrlType
                                .CTRL_DISABLE.getValue())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(notCtrlList)) {
                            securityUser.setCtrlType(CtrlType.CTRL_DISABLE.getValue());
                        }
                        securityAuthority.setDataAuthorityList(dataAuthorityDTOList);
                    }
                }

                // 重新封装认证信息
                UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(securityUser, "", securityUser.getAuthorities());
                userAuthenticationToken.setSecurityAuthority(securityAuthority);
                SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
            }
        } catch (Exception e) {
            log.error("LoadAuthorityFilter error:", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
