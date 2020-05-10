package com.biyao.annotation.aop;

import com.biyao.entities.KafkaBaseDTO;
import com.biyao.multi.TenantDataSourceProvider;
import com.biyao.util.JsonUtil;
import com.biyao.util.ThreadTenantUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 功能描述 kafka监听设置租户号
 *
 * @author hxs
 * @date 2020/5/10
 */
@Component
@Aspect
public class TenantKafkaListenerAspect {

    private final Logger log = LoggerFactory.getLogger(TenantKafkaListenerAspect.class);

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    private void tenantKafkaListenerAspect() {
    }

    /**
     * 切面执行前
     */
    @Before("tenantKafkaListenerAspect()")
    public void before(JoinPoint joinPoint) {
        log.debug(" invoking method {} before. ", ((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        try {
            Object[] args = joinPoint.getArgs();
            if (null == args) {
                return;
            }
            for (Object params : args) {
                setTenantCodeByHeader(params);
                if (null != ThreadTenantUtil.getTenant()) {
                    // todo
                    TenantDataSourceProvider.addInitList(ThreadTenantUtil.getTenant());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("before error:{}", e);
        }
    }

    /**
     * 设置租户信息
     */
    private void setTenantCodeByHeader(Object params) {
        // 消费者信息
        if (params instanceof ConsumerRecord) {
            ConsumerRecord<?, Object> record = (ConsumerRecord<?, Object>) params;
            String tenantCode = null;
            // 获取租户号
            if (record.value() instanceof String) {
                Optional<String> kafkaMessage = Optional.ofNullable((String) record.value());
                if (kafkaMessage.isPresent()) {
                    KafkaBaseDTO recordValue = JsonUtil.stringToObject(kafkaMessage.get(), KafkaBaseDTO.class);
                    if (null != recordValue) {
                        tenantCode = recordValue.getTenantCode();
                    }
                }
            } else if (record.value() instanceof KafkaBaseDTO) {
                Optional<KafkaBaseDTO> kafkaMessage = Optional.ofNullable((KafkaBaseDTO) record.value());
                if (kafkaMessage.isPresent()) {
                    tenantCode = kafkaMessage.get().getTenantCode();
                }
            }
            ThreadTenantUtil.setTenant(tenantCode);
        }
    }

    /**
     * 切面执行后
     */
    @After("tenantKafkaListenerAspect()")
    public void after(JoinPoint joinPoint) {
        log.debug("invoking method {} after.",
                ((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        if (null != ThreadTenantUtil.getTenant()) {
            TenantDataSourceProvider.removeInitList(ThreadTenantUtil.getTenant());
            ThreadTenantUtil.remove();
        }
    }
}