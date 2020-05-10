package com.biyao.config;


import com.biyao.config.properties.MongoProperties;
import com.biyao.multi.mongo.TenantMongoDatabaseProvider;
import com.biyao.util.ThreadTenantUtil;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.ServerAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.biyao.multi.TenantDataSourceProvider.DEFAULT_KEY;


/**
 * @Author tanglh
 * @Date 2018/11/30 19:36
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = {"host", "port", "dbname"})
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Autowired
    private MongoProperties mongoProperties;

    @Override
    public Mongo mongo() {
        return getMongo(getDatabaseName());
    }

    /**
     * 获取mongo
     * @param dbName
     * @return
     */
    public Mongo getMongo(String dbName) {
        TenantMongoDatabaseProvider.setDatabase(DEFAULT_KEY, mongoProperties.getDbName());
        //线上RDS配置
        if (StringUtils.isNotBlank(mongoProperties.getReplicaSet())) {
            List<ServerAddress> serverAddressList = new ArrayList<>();
            mongoProperties.getHost().forEach(host -> serverAddressList.add(new ServerAddress(host, mongoProperties.getPort())));
            StringBuilder uri = new StringBuilder();
            uri.append("mongodb://").append(mongoProperties.getName())
                    .append(":").append(mongoProperties.getPassword()).append("@")
                    .append(StringUtils.join(serverAddressList, ",")).append("/")
                    .append(dbName).append("?replicaSet=").append(mongoProperties.getReplicaSet());
            //return new Mongo(seedList, options); //该方法连接阿里云mongodb ，会爆出授权错误
            return new Mongo(new MongoURI(uri.toString()));
        }
        //线下环境
        return new Mongo(mongoProperties.getHost().get(0), mongoProperties.getPort());
    }

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        return mongoDbFactory().getMongoTemplate();
    }

    /**
     * 覆盖默认的MongoDbFactory
     *
     * @return
     */
    @Bean
    @Override
    public MultiTenantMongoDbFactory mongoDbFactory() {
        MultiTenantMongoDbFactory mongoDbFactory = new MultiTenantMongoDbFactory(mongo(), mongoProperties.getDbName());
        mongoDbFactory.setMongoTemplate(new MongoTemplate(mongoDbFactory));
        return mongoDbFactory;
    }


    /**
     * 获取当前数据库名
     *
     * @return
     */
    @Override
    protected String getDatabaseName() {
        return Optional.ofNullable(ThreadTenantUtil.getTenant()).orElse(mongoProperties.getDbName());
    }


    @Bean
    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateToZonedDateTimeConverter());
        converters.add(new ZonedDateTimeToDateConverter());
        return new CustomConversions(converters);
    }

    static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Date source) {
            return ZonedDateTime.ofInstant(source.toInstant(),
                    ZoneId.of("Asia/Shanghai"));
        }
    }

    static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
        @Override
        public Date convert(ZonedDateTime source) {
            return Date.from(source.toInstant());
        }
    }
}
