package com.biyao.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 本地配置的数据源 需要与默认数据源在同一个库下
 * ignoreInvalidFields:当我们为属性配置错误的值时，而又不希望 Spring Boot 应用启动失败
 *
 * @author: hxs
 * @create: 2020/4/10
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.local", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class LocalDataSourceProperties {

    private Map<String, String> properties = new HashMap<>();

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
