package com.biyao.multi.kafka;

import com.biyao.entities.BusinessPartInitNotifyDTO;
import com.biyao.entities.DataSourceInfo;
import com.biyao.service.InitTenantService;
import com.biyao.service.TenantDataSourceService;
import com.biyao.service.TopicMessageService;
import com.biyao.service.impl.SimpleInitTenantServiceImpl;
import com.biyao.util.IPUtil;
import com.biyao.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 功能描述 kafka初始化数据库
 *
 * @author hxs
 * @date 2020/5/15
 */
@Component
@ConditionalOnProperty("spring.kafka.bootstrap-servers")
public class TenantDataSourceKafkaListener {

    private final Logger log = LoggerFactory.getLogger(TenantDataSourceKafkaListener.class);

    @Value("${eureka.instance.appname}")
    private String APP_NAME;
    @Value("${eureka.instance.instanceId}")
    private String instanceId;
    @Value("${spring.kafka.topics.tenant-data-source-info-callback:}")
    private String tenantDataSourceInfoCallbackTopic;
    @Value("${spring.kafka.topics.tenant-data-source-info}")
    private String tenantDataSourceInfo;

    @Autowired(required = false)
    private List<InitTenantService> initTenantServices;

    @Autowired
    private TenantDataSourceService tenantDataSourceService;

    @Autowired(required = false)
    private TopicMessageService topicMessageService;

    @Autowired
    private SimpleInitTenantServiceImpl simpleInitTenantService;

    /**
     * 监听通道信息 id生成 兼容低版本 group与高版本groupId
     */
    @KafkaListener(
            id = "tenantDataSourceKafkaListener-${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topics.tenant-data-source-info}")
    public void tenantDataSourceKafkaListener(ConsumerRecord<?, String> record) {
        log.info("tenantDataSourceKafkaListener:{}", record);
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        kafkaMessage.ifPresent(message -> initTenantInfo(JsonUtil.stringToObject(message, DataSourceInfo.class)));
    }

    /**
     * 初始化
     */
    public void initTenantInfo(DataSourceInfo dataSourceInfo) {
        if (null == dataSourceInfo) {
            log.debug("notify dataSourceInfo is empty.");
            return;
        }
        if (!APP_NAME.equalsIgnoreCase(dataSourceInfo.getServerName())) {
            log.debug("is not this server:{} notify.", APP_NAME);
            return;
        }
        // 初始化租户数据库Service
        if (CollectionUtils.isEmpty(initTenantServices)) {
            log.warn("initTenantServices is empty, cannot init.");
            return;
        }
        // 数据库是否已存在
        if (tenantDataSourceService.checkDataSource(dataSourceInfo.getTenantCode(), dataSourceInfo.getType())) {
            log.info("tenant datasource is exist");
            return;
        }
        simpleInitTenantService.initTenantInfo(dataSourceInfo);
        BusinessPartInitNotifyDTO callbackNotify = new BusinessPartInitNotifyDTO();
        callbackNotify.setAppName(APP_NAME);
        callbackNotify.setInstanceId(instanceId);
        callbackNotify.setTenantCode(dataSourceInfo.getTenantCode());
        callbackNotify.setIp(IPUtil.getServerIp());
        callbackNotify.setType(dataSourceInfo.getType());
        StringBuilder detail = new StringBuilder();
        try {
            // 设置数据库编码
            tenantDataSourceService.setDatabaseCharsetToConfig(dataSourceInfo.getDatabase());
            initTenantServices.forEach(initTenantService -> {
                if (!simpleInitTenantService.equals(initTenantService)) {
                    callbackNotify.setSuccess(initTenantService.initTenantInfo(dataSourceInfo));
                }
            });
        } catch (Exception e) {
            log.warn("init error:{}", e);
            detail.append(e);
            callbackNotify.setSuccess(false);
        } finally {
            tenantDataSourceService.setDatabaseCharsetToUtf8mb4(dataSourceInfo.getDatabase());
        }
        callbackNotify.setDetail(detail.toString());
        if (null != topicMessageService && StringUtils.isNotBlank(tenantDataSourceInfoCallbackTopic)) {
            topicMessageService.sendAsync(tenantDataSourceInfoCallbackTopic, callbackNotify);
        }
    }
}
