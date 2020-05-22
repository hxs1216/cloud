package com.biyao.security.filter;

import com.taoqi.security.config.SecurityProperties;
import com.taoqi.security.provider.SecurityAuthority;
import com.taoqi.security.provider.SecurityUser;
import com.taoqi.security.provider.UserAuthenticationToken;
import com.taoqi.security.provider.transfer.OAuth2AuthorizedAccepter;
import org.apache.commons.lang3.BooleanUtils;
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

/**
 * @ClassName: ResolverAuthorizedFilter
 * @Description: 解析认证信息过滤器
 * @author tyjuncai
 * @date 2018/12/21 14:50
 */
public class ResolverAuthorizedFilter extends GenericFilterBean {
	private final Logger log = LoggerFactory.getLogger(ResolverAuthorizedFilter.class);

	private final OAuth2AuthorizedAccepter auth2AuthorizedAccepter;

	private final SecurityProperties securityProperties;

	public ResolverAuthorizedFilter(OAuth2AuthorizedAccepter auth2AuthorizedAccepter, SecurityProperties securityProperties) {
		this.auth2AuthorizedAccepter = auth2AuthorizedAccepter;
		this.securityProperties = securityProperties;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		try {
			Boolean allowedTransfer = securityProperties.getSecurity().getClientAuthorization().getAllowedReceive();
			if (BooleanUtils.isTrue(allowedTransfer)) {
				SecurityUser securityUser = auth2AuthorizedAccepter.receiveAuthentication(httpServletRequest);
				SecurityAuthority securityAuthority = auth2AuthorizedAccepter.receiveAuthority(httpServletRequest);

				if (securityUser != null) {
					UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(securityUser, "", securityUser.getAuthorities());
					if (securityAuthority != null) {
						userAuthenticationToken.setSecurityAuthority(securityAuthority);
					}
					SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
				}
			}
		} catch (Exception e) {
			log.error("ResolverAuthorizedFilter error:", e);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}
