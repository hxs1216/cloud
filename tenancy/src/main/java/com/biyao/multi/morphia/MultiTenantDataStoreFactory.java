package com.biyao.multi.morphia;

import com.biyao.config.MorphiaConfiguration;
import com.biyao.multi.mongo.TenantMongoDatabaseProvider;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.biyao.multi.TenantDataSourceProvider.DEFAULT_KEY;


@Component
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = {"host", "port", "dbname", "base.package"})
public class MultiTenantDataStoreFactory implements DataStoreFactory {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantDataStoreFactory.class);


    @Autowired
    private MorphiaConfiguration morphiaConfig;

    @Autowired
    private Datastore datastore;

    private static final Map<String, Datastore> dataStoreMap = new ConcurrentHashMap<>();

    private static boolean INDEXING_REQUIRED = Boolean.TRUE;

    @Override
    public Datastore get() {
        if (!dataStoreMap.containsKey(DEFAULT_KEY)) {
            dataStoreMap.putIfAbsent(DEFAULT_KEY, datastore);
        }
        Datastore tenantDS = getTenantDataStore();
        return tenantDS == null ? datastore : tenantDS;
    }

    /**
     * 获取dataStore
     */
    private Datastore getTenantDataStore() {
        String tenantDBName = Optional.ofNullable(TenantMongoDatabaseProvider.getDatabase()).orElse(TenantMongoDatabaseProvider.getDefaultDbName());
        Datastore tenantDS;
        synchronized (MultiTenantDataStoreFactory.class) {
            tenantDS = dataStoreMap.get(tenantDBName);
            if (tenantDS == null) {
                tenantDS = morphiaConfig.createDataStore(morphiaConfig.createMongoClient(tenantDBName), tenantDBName, INDEXING_REQUIRED);
                dataStoreMap.put(tenantDBName, tenantDS);
            }
        }
        log.debug("acquiring database: {}", tenantDBName);
        return tenantDS;
    }

}
