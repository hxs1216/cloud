package com.biyao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * kafka基础数据传输对象
 *
 * @author: hxs
 * @create: 2020/4/10
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaBaseDTO {

    /**
     * 租户号 | 业务方id
     */
    private String tenantCode;

    public String getTenantCode() {
        return tenantCode;
    }

    public KafkaBaseDTO setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"tenantCode\":\"").append(tenantCode).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
