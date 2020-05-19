package com.biyao.security.config;

import com.taoqi.security.exception.SecurityAccessDeniedHandler;
import com.taoqi.security.exception.SecurityAuthenticateEntryPoint;
import com.taoqi.security.filter.LoadAuthorityFilterConfigurer;
import com.taoqi.security.filter.RefreshTokenFilterConfigurer;
import com.taoqi.security.filter.ValidAuthorityFilterConfigurer;
import com.taoqi.security.provider.transfer.AuthorizedBearerTokenExtractor;
import com.taoqi.security.service.OAuth2AuthenticationService;
import com.taoqi.security.service.OAuth2AuthorityService;
import com.taoqi.security.service.OAuth2RemoteTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @ClassName: OAuth2ResourceConfiguration
 * @Description: 资源服务器配置
 * @author tyjuncai
 * @date 2018/12/13 10:30
 */
@Configuration
@EnableResourceServer
@ConditionalOnProperty(name = {"uaa.security.client-authorization.uaa-uri", "uaa.security.client-authorization.uaa-authority-uri"})
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
	private OAuth2AuthenticationService authenticationService;

	@Autowired
	private OAuth2AuthorityService authorityService;

	@Autowired
	private OAuth2RemoteTokenService OAuth2RemoteTokenService;

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * 资源服务器HttpSecurity配置
	 *
	 * @param http
	 * @throws Exception
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/auth/login").permitAll()
			.antMatchers("/auth/logout").authenticated()
			.and()
			.apply(new ValidAuthorityFilterConfigurer(authorityService, accessDeniedHandler(), securityProperties))
			.and()
			.apply(new LoadAuthorityFilterConfigurer(authorityService, securityProperties))
			.and()
			.apply(new RefreshTokenFilterConfigurer(authenticationService, securityProperties))
			;
	}

	/**
	 * 资源服务器认证配置
	 *
	 * @param resources
	 * @throws Exception
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationEntryPoint(authenticationEntryPoint())
				 .tokenExtractor(tokenExtractor())
				 .tokenServices(OAuth2RemoteTokenService)
				 .accessDeniedHandler(accessDeniedHandler());
	}

	/**
	 * Token提取器
	 *
	 */
	@Bean
	public TokenExtractor tokenExtractor() {
		return new AuthorizedBearerTokenExtractor();
	}

	/**
	 * 鉴权失败异常处理
	 *
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new SecurityAuthenticateEntryPoint();
	}


	/**
	 * 拒绝访问异常处理
	 *
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new SecurityAccessDeniedHandler();
	}}
