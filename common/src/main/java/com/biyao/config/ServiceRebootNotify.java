package com.biyao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * 功能描述 项目启动
 *
 * @author hxs
 * @date 2020/5/12
 */
@Order(0)
@Configuration
public class ServiceRebootNotify implements BeanPostProcessor, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(ServiceRebootNotify.class);

    @PostConstruct
    public void init() {
        log.debug("容器初始化");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void readyNotify(ApplicationReadyEvent event) {
        log.debug("项目启动完成");
    }

    @EventListener(ApplicationFailedEvent.class)
    public void failedNotify(ContextClosedEvent event) {
        log.debug("项目启动失败");
    }

    public void destroy() {
        log.debug("项目停止运行");
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}