package com.biyao.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述 RestTemplate装配Bean
 *
 * @author hxs
 * @date 2020/5/21
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class OAuth2RestConfiguration {
    private static final Logger log = LoggerFactory.getLogger(OAuth2RestConfiguration.class);

    /**
     * 装配负载均衡RestTemplate
     */
    @Bean
    @LoadBalanced
    @Qualifier("oAuth2LoadBalancedRestTemplate")
    public RestTemplate oAuth2LoadBalancedRestTemplate() {
        log.debug("oAuth2LoadBalancedRestTemplate");
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory());
        restTemplate.setErrorHandler(responseErrorHandler());
        return restTemplate;
    }

    /**
     * 装配普通RestTemplate
     */
    @Bean
    @Qualifier("oAuth2VanillaRestTemplate")
    public RestTemplate oAuth2VanillaRestTemplate() {
        log.debug("oAuth2VanillaRestTemplate");
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory());
        restTemplate.setErrorHandler(responseErrorHandler());
        return restTemplate;
    }

    /**
     * 连接工厂
     */
    private SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);
        return factory;
    }

    /**
     * 异常处理
     */
    private ResponseErrorHandler responseErrorHandler() {
        ResponseErrorHandler responseErrorHandler = new SecurityResponseErrorHandler();
        return responseErrorHandler;
    }
}
