package com.biyao.service;

import com.biyao.config.MyKafkaSender;
import com.biyao.entities.KafkaBaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 功能描述 kafka客户端
 *
 * @author hxs
 * @date 2020/5/15
 */
@Service
@ConditionalOnProperty("spring.kafka.bootstrap-servers")
public class TopicMessageService {

    private final Logger log = LoggerFactory.getLogger(TopicMessageService.class);

    @Autowired
    private MyKafkaSender<KafkaBaseDTO> myKafkaSender;

    /**
     * 同步 发送
     *
     */
    public <T extends KafkaBaseDTO> void sendSync(String topic, T t) {
        log.debug("sync send message:{},{},{}", topic, t.getTenantCode(), t);
        myKafkaSender.sendMessageSync(topic, t);
    }

    /**
     * 异步 发送
     *
     */
    public <T extends KafkaBaseDTO> void sendAsync(String topic, T t) {
        log.debug("sync send message:{},{},{}", topic, t.getTenantCode(), t);
        myKafkaSender.sendMessageAsync(topic, t);
    }

    /**
     * 同步 发送
     *
     */
    public <T extends KafkaBaseDTO> void sendSync(String topic, String tenantCode, T t) {
        log.debug("sync send message:{},{},{}", topic, tenantCode, t);
        myKafkaSender.sendMessageSync(topic, tenantCode, t);
    }

    /**
     * 异步 发送
     *
     */
    public <T extends KafkaBaseDTO> void sendAsync(String topic, String tenantCode, T t) {
        log.debug("async send message:{},{},{}", topic, tenantCode, t);
        myKafkaSender.sendMessageAsync(topic, tenantCode, t);
    }

}