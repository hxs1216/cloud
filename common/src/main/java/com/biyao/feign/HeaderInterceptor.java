package com.biyao.feign;

import com.biyao.util.ThreadTenantUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * feign的header拦截器
 *
 * @description: feign的header拦截器
 * @author: hxs
 * @create: 2020/4/10
 */
public class HeaderInterceptor implements RequestInterceptor {
    private final Set<String> HEADER_NAMES = new HashSet(Arrays.asList("tenant_code", "oauth2-authentication", "oauth2-authority"));

    public HeaderInterceptor(String... args) {
        this.HEADER_NAMES.addAll(Arrays.asList(args));
    }

    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("tenant_code", new String[]{ThreadTenantUtil.getTenant()});
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes && null != attributes.getRequest()) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = (String) headerNames.nextElement();
                    if (this.HEADER_NAMES.contains(headerName)) {
                        String value = request.getHeader(headerName);
                        requestTemplate.header(headerName, new String[]{value});
                    }
                }
            }
        }
    }
}