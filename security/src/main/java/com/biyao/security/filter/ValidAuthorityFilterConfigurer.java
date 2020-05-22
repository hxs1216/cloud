package com.biyao.security.filter;

import com.taoqi.security.config.SecurityProperties;
import com.taoqi.security.service.OAuth2AuthorityService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @ClassName: ValidAuthorityFilterConfigurer
 * @Description: 校验权限过滤器加载装配
 * @author tyjuncai
 * @date 2018/12/20 16:51
 */
public class ValidAuthorityFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private OAuth2AuthorityService authorityService;

	private AccessDeniedHandler accessDeniedHandler;

	private SecurityProperties securityProperties;

	public ValidAuthorityFilterConfigurer(OAuth2AuthorityService authorityService, AccessDeniedHandler accessDeniedHandler, SecurityProperties securityProperties) {
		this.authorityService = authorityService;
		this.accessDeniedHandler = accessDeniedHandler;
		this.securityProperties = securityProperties;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 校验权限过滤器
		ValidAuthorityFilter validAuthorityFilter = new ValidAuthorityFilter(authorityService, accessDeniedHandler, securityProperties);
		http.addFilterAfter(validAuthorityFilter, OAuth2AuthenticationProcessingFilter.class);
	}
}
