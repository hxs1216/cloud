package com.biyao.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * feign配置
 *
 * @description: feign配置
 * @author: hxs
 * @create: 2020/4/10
 */
public class FeignConfig {

    /**
     * 服务间需要透传的头
     */
    private static final String[] HEADER_NAMES = new String[]{"micro-service", "tq_app_id", "user_id", "client_b_v", "os", "imei", "token", "phone", "platform", "uuid"};

    @Bean
    @Primary
    public RequestInterceptor requestInterceptor() {
        return new HeaderInterceptor(HEADER_NAMES);
    }
}