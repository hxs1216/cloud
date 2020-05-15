package com.biyao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 业务方初始化通知
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessPartInitNotifyDTO extends KafkaBaseDTO {

    /**
     * 微服务名称
     */
    private String appName;

    /**
     * 微服务实例id
     */
    private String instanceId;

    /**
     * 微服务实例所在ip
     */
    private String ip;

    /**
     * 是否初始化成功
     */
    private Boolean success;

    /**
     * 初始化失败的结果详情
     */
    private String detail;

    /**
     * 数据库类型
     */
    private String type;

    public String getAppName() {
        return appName;
    }

    public BusinessPartInitNotifyDTO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public BusinessPartInitNotifyDTO setInstanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public BusinessPartInitNotifyDTO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public BusinessPartInitNotifyDTO setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public BusinessPartInitNotifyDTO setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"appName\":\"").append(appName).append('\"');
        sb.append(",\"instanceId\":\"").append(instanceId).append('\"');
        sb.append(",\"ip\":\"").append(ip).append('\"');
        sb.append(",\"success\":").append(success);
        sb.append(",\"detail\":\"").append(detail).append('\"');
        sb.append(",\"type\":\"").append(type).append('\"');
        sb.append('}');
        sb.append(super.toString());
        return sb.toString();
    }
}
