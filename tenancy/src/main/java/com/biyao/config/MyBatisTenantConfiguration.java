package com.biyao.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.biyao.multi.DynamicDataSource;
import com.biyao.multi.TenantDataSourceProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * mybatis多租户配置
 *
 * @Author tanglh
 * @Date 2019/9/11 13:38
 */
@Configuration
@ConditionalOnProperty(prefix = "tenancy.client", name = {"enable", "type"})
@ConditionalOnExpression("'${tenancy.client.type}'.equals('mybatis') || '${tenancy.client.type}'.equals('mybatis-plus')")
public class MyBatisTenantConfiguration {

    private static final String MYBATIS = "mybatis";

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;
    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;
    @Value("${tenancy.client.type}")
    private String tenancyClientType;
    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
        dataSource.setJdbcUrl(url);
        return dataSource;
    }

    /**
     * 动态数据源
     */
    @Bean
    public DynamicDataSource dynamicDataSource(DataSource dataSource) {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        Map<Object, Object> map = new HashMap<>(1);
        map.put(TenantDataSourceProvider.DEFAULT_KEY, dataSource);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultTargetDataSource(dataSource);
        return dynamicDataSource;
    }

    /**
     * 动态数据源创建SqlSessionFactory
     */
    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        if (MYBATIS.equals(tenancyClientType)) {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dynamicDataSource);
            bean.setTypeAliasesPackage(typeAliasesPackage);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources(mapperLocations));
            return bean.getObject();
        } else {
            MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
            bean.setDataSource(dynamicDataSource);
            bean.setTypeAliasesPackage(typeAliasesPackage);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources(mapperLocations));
            return bean.getObject();
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}