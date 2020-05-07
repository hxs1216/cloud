package com.biyao.config;

import com.biyao.multi.TenantDataSourceProvider;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 * 多租户配置
 *
 * @author: hxs
 * @create: 2020/5/6
 */
@Configuration
@Order(-1)
@EnableAsync
@Slf4j
public class TenantConfiguration extends WebMvcConfigurerAdapter implements ApplicationRunner, ServletContextInitializer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TenantDataSourceProvider tenantDataSourceProvider;

    @Autowired(required = false)
    private LiquibaseProperties liquibaseProperties;

    @Value("${liquibase.enable:false}")
    private boolean liquibaseEnable;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (null == TenantDataSourceProvider.getTenantDataSource(TenantDataSourceProvider.DEFAULT_KEY)) {
            log.info("init defaultDatasource on Application starting");
            tenantDataSourceProvider.addDefaultDataSource(dataSource);
        } else {
            log.debug("defaultDatasource has init success.");
        }
    }

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
}