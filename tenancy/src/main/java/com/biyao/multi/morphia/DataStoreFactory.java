package com.biyao.multi.morphia;

import org.mongodb.morphia.Datastore;

/**
 * @Author tanglh
 * @Date 2018/12/29 11:11
 */
public interface DataStoreFactory {
    /**
     * 获取数据源
     *
     * @return
     */
    Datastore get();
}
