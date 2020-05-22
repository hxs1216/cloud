package com.biyao.security.filter;

import com.taoqi.common.constants.UserType;
import com.taoqi.security.config.SecurityProperties;
import com.taoqi.security.dto.BackendAuthorityDTO;
import com.taoqi.security.provider.SecurityUser;
import com.taoqi.security.provider.SecurityUtils;
import com.taoqi.security.service.OAuth2AuthorityService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tyjuncai
 * @ClassName: ValidAuthorityFilter
 * @Description: 校验权限过滤器
 * @date 2018/12/20 9:50
 */
public class ValidAuthorityFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(ValidAuthorityFilter.class);

    private final OAuth2AuthorityService authorityService;

    private final SecurityProperties securityProperties;

	private final AccessDeniedHandler accessDeniedHandler;

    public ValidAuthorityFilter(OAuth2AuthorityService authorityService, AccessDeniedHandler accessDeniedHandler, SecurityProperties securityProperties) {
        this.authorityService = authorityService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.securityProperties = securityProperties;

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

	    // 获取当前登录用户
	    Optional<SecurityUser> securityUserOp = SecurityUtils.getCurrentUser();
	    final Boolean[] hasAuth = new Boolean[1];
	    securityUserOp.ifPresent(securityUser -> {
	    	if (ObjectUtils.equals(securityUser.getType(), UserType.business.getValue())) {
			    List<BackendAuthorityDTO> backendAuthorityDTOList = authorityService.getAuthorityUserIdsMap();

			    // 这里可能匹配到多条
			    List<BackendAuthorityDTO> backendAuthorityDTOOpt = backendAuthorityDTOList.stream()
						.filter(o -> StringUtils.isNotBlank(o.getUrl()))
					    .filter(backendAuthDTO -> {
						    RequestMatcher requestMatcher = new AntPathRequestMatcher(backendAuthDTO.getUrl(), backendAuthDTO.getMethod());
						    return requestMatcher.matches(httpServletRequest);
					    }).collect(Collectors.toList());

				if (CollectionUtils.isEmpty(backendAuthorityDTOOpt)) {
					hasAuth[0] = true;
				} else {
					// userId 做并集
					Set<Long> hasAuthUserIds = new HashSet<>();
					backendAuthorityDTOOpt.forEach(o -> {
						if (CollectionUtils.isNotEmpty(o.getUserIds())) {
							hasAuthUserIds.addAll(o.getUserIds());
						}
					});
					hasAuthUserIds.remove(null);
					hasAuth[0] = hasAuthUserIds.contains(securityUser.getUserId());
				}
			}
		});

	    if (BooleanUtils.isFalse(hasAuth[0])) {
		    accessDeniedHandler.handle(httpServletRequest, httpServletResponse, new AccessDeniedException("Access is denied"));
			return;
	    }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
