package com.biyao.multi.morphia;

import org.mongodb.morphia.Datastore;


public interface DataStoreFactory {

    /**
     * 获取数据源
     *
     */
    Datastore get();
}
