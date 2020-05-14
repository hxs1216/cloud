package com.biyao.config;

import com.biyao.interceptor.TenantInterceptor;
import com.biyao.interceptor.TenantMissHandleInterface;
import com.biyao.multi.TenantDataSourceProvider;
import com.biyao.service.TenantDataSourceService;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 多租户配置
 * ApplicationRunner：服务启动后初始化远程配置
 * WebMvcConfigurerAdapter：添加过滤器
 *
 * @author: hxs
 * @create: 2020/5/6
 */
@Configuration
@Order(-1)
@EnableAsync
public class TenantConfiguration extends WebMvcConfigurerAdapter
        implements ApplicationRunner, ServletContextInitializer {

    private static final Logger log = LoggerFactory.getLogger(TenantConfiguration.class);

    @Value("${liquibase.enable:false}")
    private boolean liquibaseEnable;

    @Autowired
    @Lazy
    private TenantDataSourceService tenantDataSourceService;

    @Autowired
    private TenantDataSourceProvider tenantDataSourceProvider;

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private LiquibaseProperties liquibaseProperties;

    @Autowired(required = false)
    private Set<TenantMissHandleInterface> tenantMissHandleInterfaces;

    /**
     * 多租户过滤器不过滤url
     */
    private final String[] excludes = {
            "/index.html",
            "/v2/api-docs"
    };

    @Bean("tenantLiquibase")
    public SpringLiquibase tenantLiquibase() {
        SpringLiquibase liquibase = new TenantSpringLiquibase();
        if (null == liquibaseProperties) {
            return liquibase;
        }
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        Boolean shouldRun = liquibaseEnable || liquibaseProperties.isEnabled();
        liquibase.setShouldRun(shouldRun);
        return liquibase;
    }

    /**
     * 功能描述 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("add TenantInterceptor");
        List<String> allExcludes = new ArrayList<>();
        allExcludes.addAll(Arrays.asList(excludes));
        registry.addInterceptor(new TenantInterceptor(tenantDataSourceService, tenantMissHandleInterfaces))
                .addPathPatterns("/**").excludePathPatterns(allExcludes.toArray(new String[allExcludes.size()]));
    }

    /**
     * 服务启动后初始化远程配置
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.debug("init datasource on Application started");
        tenantDataSourceService.initRemoteDataSourceAsync();
        log.debug("init remote datasource success");
    }

    /**
     * 功能描述 添加默认数据源
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (null == TenantDataSourceProvider.getTenantDataSource(TenantDataSourceProvider.DEFAULT_KEY)) {
            log.info("init defaultDatasource on Application starting");
            tenantDataSourceProvider.addDefaultDataSource(dataSource);
        } else {
            log.debug("defaultDatasource has init success.");
        }
    }
}