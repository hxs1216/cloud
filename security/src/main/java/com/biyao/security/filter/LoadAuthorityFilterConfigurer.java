package com.biyao.security.filter;

import com.biyao.security.config.SecurityProperties;
import com.biyao.security.service.OAuth2AuthorityService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 功能描述 获取权限过滤器加载装配
 *
 * @author hxs
 * @date 2020/5/22
 */
public class LoadAuthorityFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private OAuth2AuthorityService authorityService;

    private SecurityProperties securityProperties;

    public LoadAuthorityFilterConfigurer(OAuth2AuthorityService authorityService, SecurityProperties securityProperties) {
        this.authorityService = authorityService;
        this.securityProperties = securityProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 获取权限过滤器
        LoadAuthorityFilter loadAuthorityFilter = new LoadAuthorityFilter(authorityService, securityProperties);
        http.addFilterAfter(loadAuthorityFilter, ValidAuthorityFilter.class);
    }
}
