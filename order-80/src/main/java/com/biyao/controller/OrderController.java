package com.biyao.controller;

import com.biyao.entities.CommonResult;
import com.biyao.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * Title: 订单controller
 * Description: 订单controller
 * Copyright: Copyright (c) 2019
 * Company: TaoQiCar Co., Ltd.
 * Create Time: 2020/3/22
 *
 * @author hxs
 * @email huangxiaosheng@xxfqc.com
 * Update Time:
 * Updater:
 * Update Comments:
 */
@RestController
@Slf4j
public class OrderController {
    private final static String PAYMENT_URL = "http://PAYMENT-SERVICE";//集群

    @Autowired
    private RestTemplate restTemplate;
//    @Autowired
//    private LoadBalancer loadBalancer;
//    @Autowired
//    private DiscoveryClient discoveryClient;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class, id);
    }

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

//    @GetMapping("/consumer/payment/lb")
//    public String getPaymentLB() {
//        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
//        if (instances == null || instances.size() <= 0) {
//            return null;
//        }
//
//        ServiceInstance serviceInstance = loadBalancer.instances(instances);
//        URI uri = serviceInstance.getUri();
//
//        return restTemplate.getForObject(uri + "/payment/lb", String.class);
//    }
} 