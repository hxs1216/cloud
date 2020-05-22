package com.biyao.security.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 功能描述 用户认证对象
 *
 * @author hxs
 * @date 2020/5/22
 */
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private SecurityAuthority securityAuthority;

    public UserAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public SecurityAuthority getSecurityAuthority() {
        return securityAuthority;
    }

    public void setSecurityAuthority(SecurityAuthority securityAuthority) {
        this.securityAuthority = securityAuthority;
    }
}
