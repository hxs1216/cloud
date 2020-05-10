package com.biyao.config;

import com.biyao.config.properties.MongoProperties;
import com.biyao.multi.mongo.TenantMongoDatabaseProvider;
import com.biyao.util.ThreadTenantUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.Converters;
import org.mongodb.morphia.converters.DateConverter;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.biyao.multi.TenantDataSourceProvider.DEFAULT_KEY;

/**
 * 功能描述 springboot通过Morphia获取mongoDB数据
 *
 * @author hxs
 * @date 2020/5/10
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = {"host", "port", "dbname", "base.package", "multi-enable"})
@ConditionalOnExpression(value = "${spring.data.mongodb.multi-enable}")
public class MorphiaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MorphiaConfiguration.class);

    @Autowired
    private MongoProperties mongoProperties;

    @Value("${spring.data.mongodb.base.package}")
    private String basePackage;

    public MorphiaConfiguration() {
    }

    @Bean
    public MongoClient mongoClient() {
        return createMongoClient(getDatabaseName());
    }

    /**
     * 功能描述 创建mongodb客户端
     *
     * @param dbName 数据库名称
     * @return com.mongodb.MongoClient
     * @author hxs
     * @date 2020/5/10
     */
    public MongoClient createMongoClient(String dbName) {
        log.info("createMongoClient:{}", dbName);
        // 线上RDS配置
        if (StringUtils.isNotBlank(mongoProperties.getReplicaSet())) {
            log.info("createMongoClient of prod");
            List<ServerAddress> serverAddressList = new ArrayList<>();
            mongoProperties.getHost().forEach(host -> serverAddressList.add(new ServerAddress(host, mongoProperties.getPort())));
            StringBuilder uri = new StringBuilder();
            uri.append("mongodb://").append(mongoProperties.getName())
                    .append(":").append(mongoProperties.getPassword()).append("@")
                    .append(StringUtils.join(serverAddressList, ",")).append("/")
                    .append(dbName).append("?replicaSet=").append(mongoProperties.getReplicaSet());
            //return new Mongo(seedList, options); //该方法连接阿里云mongodb ，会爆出授权错误
            return new MongoClient(new MongoClientURI(uri.toString()));
        }
        //线下环境
        return new MongoClient(mongoProperties.getHost().get(0), mongoProperties.getPort());
    }

    private String getDatabaseName() {
        return Optional.ofNullable(ThreadTenantUtil.getTenant()).orElse(mongoProperties.getDbName());
    }

    @Bean
    public Datastore datastore() {
        TenantMongoDatabaseProvider.setDatabase(DEFAULT_KEY, mongoProperties.getDbName());
        return createDataStore(mongoClient(), mongoProperties.getDbName(), Boolean.TRUE);
    }

    public Datastore createDataStore(MongoClient mongoClient, String dbName,
                                     boolean indexRequired) {
        Mapper mapper = new Mapper();
        Converters converters = mapper.getConverters();
        converters.addConverter(new ZoneDateTimeConverter());
        Morphia morphia = new Morphia(mapper);
        Datastore datastore = morphia.createDatastore(mongoClient, dbName);
        if (indexRequired) {
            datastore.ensureIndexes();
        }
        return datastore;
    }

    private static final class ZoneDateTimeConverter extends DateConverter implements SimpleValueConverter {
        /**
         * Creates the Converter.
         */
        public ZoneDateTimeConverter() {
            this(ZonedDateTime.class);
        }

        protected ZoneDateTimeConverter(final Class clazz) {
            super(clazz);
        }

        @Override
        public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {
            Date date = (Date) super.decode(targetClass, val, optionalExtraInfo);
            return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

        @Override
        public Object encode(Object value, MappedField optionalExtraInfo) {
            return super.encode(Date.from(((ZonedDateTime) value).toInstant()), optionalExtraInfo);
        }
    }
}
