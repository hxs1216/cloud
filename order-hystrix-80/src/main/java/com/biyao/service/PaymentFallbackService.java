package com.biyao.service;

import org.springframework.stereotype.Component;

/**
 * @title: feign接口实现类
 * @description:
 * @author: hxs
 * @create: 2020/4/8
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{

    @Override
    public String paymentInfo_OK(Integer id) {
        return "----PaymentFallbackService fall back--paymentInfo_OK";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "----PaymentFallbackService fall back--paymentInfo_TimeOut";
    }
}