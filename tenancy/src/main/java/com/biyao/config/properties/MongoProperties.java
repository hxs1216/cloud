package com.biyao.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 功能描述 mongodb配置
 *
 * @author hxs
 * @date 2020/5/10
 */
@Component
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = {"host", "port", "dbname"})
public class MongoProperties {

    @Value("${spring.data.mongodb.uri:#{null}}")
    private String uri;

    @Value("#{'${spring.data.mongodb.host}'.split(',')}")
    private List<String> host;

    @Value("${spring.data.mongodb.port}")
    private Integer port;

    @Value("${spring.data.mongodb.dbname}")
    private String dbName;

    @Value("${spring.data.mongodb.username}")
    private String name;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.replica-set:}")
    private String replicaSet;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }
}
