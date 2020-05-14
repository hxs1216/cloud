package com.biyao.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.biyao.constants.Constants;
import com.biyao.error.CloudException;
import com.biyao.error.CommonException;
import com.biyao.error.ErrorCode;
import com.biyao.service.TenantDataSourceService;
import com.biyao.util.ThreadTenantUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 功能描述 租户拦截器
 *
 * @author hxs
 * @date 2020/5/13
 */
public class TenantInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    private final TenantDataSourceService tenantDataSourceService;

    private final Set<TenantMissHandleInterface> tenantMissHandleInterfaces;

    public TenantInterceptor(TenantDataSourceService tenantDataSourceService, Set<TenantMissHandleInterface> tenantMissHandleInterfaces) {
        this.tenantDataSourceService = tenantDataSourceService;
        this.tenantMissHandleInterfaces = tenantMissHandleInterfaces;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String tenantCode = this.getTenantCode(request);
        log.debug("TenantInterceptor setTenant:{}", tenantCode);
        if (StringUtils.isBlank(tenantCode)) {
            log.warn("current_tenant_code_not_found : uri -> {}", request.getRequestURI());
            throw CloudException.build(ErrorCode.current_tenant_code_not_found);
        }

        // 所有数据源不存在则去远程同步数据源
        if (!tenantDataSourceService.checkAllDataSource(tenantCode)) {
            tenantDataSourceService.initDataSourceInfoByTenantCode(tenantCode, Constants.PROFILE_MYSQL);
            tenantDataSourceService.initDataSourceInfoByTenantCode(tenantCode, Constants.PROFILE_MONGO);
        }

        // 再次校验所有数据源是否存在都没有配置则报异常
        if (!tenantDataSourceService.checkAllDataSource(tenantCode)) {
            throw new CommonException(ErrorCode.cannot_get_data_source);
        }

        //设置租户信息
        ThreadTenantUtil.setTenant(tenantCode);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        log.debug("TenantInterceptor removeTenant:{}", ThreadTenantUtil.getTenant());

        //释放资源
        ThreadTenantUtil.remove();
    }

    /**
     * 获取租户号
     */
    private String getTenantCode(HttpServletRequest request) {
        String tenantCode = request.getHeader(Constants.TENANT_CODE_KEY);
        if (StringUtils.isBlank(tenantCode) && CollectionUtil.isNotEmpty(tenantMissHandleInterfaces)) {
            for (TenantMissHandleInterface handleInterface : tenantMissHandleInterfaces) {
                if (handleInterface.match(request)) {
                    tenantCode = handleInterface.genTenantCode(request);
                    break;
                }
            }
        }
        return tenantCode;
    }
}