package com.biyao.security.config;

import com.taoqi.security.filter.ResolverAuthorizedFilterConfigurer;
import com.taoqi.security.provider.transfer.OAuth2AuthorizedAccepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @ClassName: OAuth2ReceiveConfiguration
 * @Description: 接收授权信息过滤器配置
 * @author tyjuncai
 * @date 2018/12/21 14:42
 */
@Configuration
@EnableResourceServer
@ConditionalOnProperty("uaa.security.client-authorization.allowed-receive")
public class OAuth2ReceiveConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
	private OAuth2AuthorizedAccepter authorizedAccepter;

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.apply(new ResolverAuthorizedFilterConfigurer(authorizedAccepter, securityProperties));
	}
}
