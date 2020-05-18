package com.biyao.multi.morphia;

import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能描述 多租户morphia 基础dao
 *
 * @author hxs
 * @date 2020/5/18
 */
public class MultiTenantBasicDAO<T, K> implements DAO<T, K> {

    private DataStoreFactory dsFactory;

    private Class<T> entityClazz;

    public MultiTenantBasicDAO(DataStoreFactory dsFactory) {
        this.dsFactory = dsFactory;
        this.entityClazz = ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public void bulkInsert(List<T> entities) {
        this.bulkInsert(entities, WriteConcern.ACKNOWLEDGED);
    }

    public void bulkInsert(List<T> entities, WriteConcern writeConcern) {
        dsFactory.get().save(entities, writeConcern);
    }

    public T findOne(Map<String, String> fieldValueMap) {
        Query<T> query = dsFactory.get().createQuery(getEntityClass());
        for (Map.Entry<String, String> fieldAndValue : fieldValueMap.entrySet()) {
            query.field(fieldAndValue.getKey()).equalIgnoreCase(
                    fieldAndValue.getValue());
        }
        return query.get();
    }

    @Override
    public long count() {
        return dsFactory.get().getCount(entityClazz);
    }

    @Override
    public long count(String key, Object value) {
        return count(dsFactory.get().find(entityClazz, key, value));
    }

    @Override
    public long count(Query<T> query) {
        return dsFactory.get().getCount(query);
    }

    @Override
    public Query<T> createQuery() {
        return dsFactory.get().createQuery(entityClazz);
    }

    @Override
    public UpdateOperations<T> createUpdateOperations() {
        return dsFactory.get().createUpdateOperations(entityClazz);
    }

    @Override
    public WriteResult delete(T entity) {
        return dsFactory.get().delete(entity);
    }

    @Override
    public WriteResult delete(T entity, WriteConcern wc) {
        return dsFactory.get().delete(entity, wc);
    }

    @Override
    public WriteResult deleteById(K id) {
        return dsFactory.get().delete(entityClazz, id);
    }

    @Override
    public WriteResult deleteByQuery(Query<T> query) {
        return dsFactory.get().delete(query);
    }

    @Override
    public void ensureIndexes() {
        dsFactory.get().ensureIndexes(entityClazz);
    }

    @Override
    public boolean exists(String key, Object value) {
        return exists(dsFactory.get().find(entityClazz, key, value));
    }

    @Override
    public boolean exists(Query<T> query) {
        return dsFactory.get().getCount(query) > 0;
    }

    @Override
    public QueryResults<T> find() {
        return createQuery();
    }

    @Override
    public QueryResults<T> find(Query<T> query) {
        return query;
    }

    @Override
    public List<K> findIds() {
        return (List<K>) keysToIds(dsFactory.get().find(entityClazz)
                .asKeyList());
    }

    @Override
    public List<K> findIds(String key, Object value) {
        return (List<K>) keysToIds(dsFactory.get()
                .find(entityClazz, key, value).asKeyList());
    }

    @Override
    public List<K> findIds(Query<T> query) {
        return (List<K>) keysToIds(query.asKeyList());
    }

    @Override
    public T findOne(String key, Object value) {
        return dsFactory.get().find(entityClazz, key, value).get();
    }

    @Override
    public T findOne(Query<T> query) {
        return query.get();
    }

    @Override
    public Key<T> findOneId() {
        return findOneId(dsFactory.get().find(entityClazz));
    }

    @Override
    public Key<T> findOneId(String key, Object value) {
        return findOneId(dsFactory.get().find(entityClazz, key, value));
    }

    @Override
    public Key<T> findOneId(Query<T> query) {
        Iterator<Key<T>> keys = query.fetchKeys().iterator();
        return keys.hasNext() ? keys.next() : null;
    }

    @Override
    public T get(K id) {
        return dsFactory.get().get(entityClazz, id);
    }

    @Override
    public DBCollection getCollection() {
        return dsFactory.get().getCollection(entityClazz);
    }

    @Override
    public Datastore getDatastore() {
        return dsFactory.get();
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClazz;
    }

    @Override
    public Key<T> save(T entity) {
        return dsFactory.get().save(entity);
    }

    @Override
    public Key<T> save(T entity, WriteConcern wc) {
        return dsFactory.get().save(entity, wc);
    }

    @Override
    public UpdateResults update(Query<T> query, UpdateOperations<T> ops) {
        return dsFactory.get().update(query, ops);
    }

    @Override
    public UpdateResults updateFirst(Query<T> query, UpdateOperations<T> ops) {
        return dsFactory.get().updateFirst(query, ops);
    }

    public Class<T> getEntityClazz() {
        return entityClazz;
    }

    /**
     * Converts from a List<Key> to their id values
     */
    protected List<?> keysToIds(final List<Key<T>> keys) {
        final List<Object> ids = new ArrayList<>(keys.size() * 2);
        ids.addAll(keys.stream().map(Key::getId).collect(Collectors.toList()));
        return ids;
    }
}