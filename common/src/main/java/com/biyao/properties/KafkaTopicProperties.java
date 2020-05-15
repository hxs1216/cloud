package com.biyao.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 功能描述 kafka配置
 *
 * @author hxs
 * @date 2020/5/15
 */
@Component
@ConditionalOnProperty("spring.kafka.bootstrap-servers")
@ConfigurationProperties(prefix = "spring.kafka", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class KafkaTopicProperties {

    private String bootstrapServers;

    private Properties topics;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Properties getTopics() {
        return topics;
    }

    public void setTopics(Properties topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("KafkaTopicProperties{");
        sb.append("bootstrapServers='").append(bootstrapServers).append('\'');
        sb.append(", topics=").append(topics);
        sb.append('}');
        return sb.toString();
    }
}
