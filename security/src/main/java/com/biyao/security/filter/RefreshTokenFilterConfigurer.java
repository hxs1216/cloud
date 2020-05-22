package com.biyao.security.filter;

import com.taoqi.security.config.SecurityProperties;
import com.taoqi.security.service.OAuth2AuthenticationService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @ClassName: RefreshTokenFilterConfigurer
 * @Description: 刷新TOKEN过滤器加载装配
 * @author tyjuncai
 * @date 2018/12/20 12:00
 */
public class RefreshTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private OAuth2AuthenticationService authenticationService;

	private SecurityProperties securityProperties;

	public RefreshTokenFilterConfigurer(OAuth2AuthenticationService authenticationService, SecurityProperties securityProperties) {
		this.authenticationService = authenticationService;
		this.securityProperties = securityProperties;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 刷新Token过滤器
		RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter(authenticationService, securityProperties);
		http.addFilterAfter(refreshTokenFilter, LoadAuthorityFilter.class);
	}
}
