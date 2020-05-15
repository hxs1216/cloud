package com.biyao.multi.mongo;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.biyao.multi.TenantDataSourceProvider.DEFAULT_KEY;


/**
 * 功能描述 多租户mongoDb工厂类
 *
 * @author hxs
 * @date 2020/5/15
 */
public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);
    private static final Map<String, String> databaseIndexMap = new ConcurrentHashMap<>();

    /**
     * 默认数据库名称
     */
    private final String defaultName;

    /**
     * MongoDB模板类
     */
    private MongoTemplate mongoTemplate;

    public MultiTenantMongoDbFactory(final Mongo mongo, final String defaultDatabaseName) {
        super(mongo, defaultDatabaseName);
        log.debug("Instantiating " + MultiTenantMongoDbFactory.class.getName() + " with default database name: " + defaultDatabaseName);
        this.defaultName = defaultDatabaseName;
    }

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public DB getDb() {
        final String dbName = databaseIndexMap.get(Optional.ofNullable(TenantMongoDatabaseProvider.getDatabase()).orElse(DEFAULT_KEY));
        final String dbToUse = (dbName != null ? dbName : this.defaultName);
        log.debug("acquiring database: {}", dbToUse);
        createIndexIfNecessaryFor(dbToUse);
        return super.getDb(dbToUse);
    }

    /**
     * 创建连接并使用
     *
     */
    public void createIndexIfNecessaryFor(final String database) {
        if (this.mongoTemplate == null) {
            log.error("MongoTemplate is null, cannot create any index.");
            return;
        }
        boolean needsToBeCreated = false;
        synchronized (MultiTenantMongoDbFactory.class) {
            final Object syncObj = databaseIndexMap.get(database);
            if (syncObj == null) {
                databaseIndexMap.put(database, database);
                needsToBeCreated = true;
            }
        }
        synchronized (databaseIndexMap.get(database)) {
            if (needsToBeCreated) {
                log.debug("Creating indices for database name=[" + database + "]");
                createIndexes();
                log.debug("Done with creating indices for database name=[" + database + "]");
            }
        }
    }

    private void createIndexes() {
        final MongoMappingContext mappingContext = (MongoMappingContext) this.mongoTemplate.getConverter().getMappingContext();
        final MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);
        for (BasicMongoPersistentEntity<?> persistentEntity : mappingContext.getPersistentEntities()) {
            checkForAndCreateIndexes(indexResolver, persistentEntity);
        }
    }

    private void checkForAndCreateIndexes(final MongoPersistentEntityIndexResolver indexResolver, final MongoPersistentEntity<?> entity) {
        if (entity.findAnnotation(Document.class) != null) {
            for (MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexDefinitionHolder : indexResolver.resolveIndexFor(entity.getTypeInformation())) {
                this.mongoTemplate.indexOps(entity.getType()).ensureIndex(indexDefinitionHolder);
            }
        }
    }
}