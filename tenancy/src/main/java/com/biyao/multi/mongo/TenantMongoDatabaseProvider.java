package com.biyao.multi.mongo;

import com.biyao.multi.TenantDataSourceProvider;
import com.biyao.util.ThreadTenantUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author hxs
 * @Date 2018/12/22 17:32
 */
public class TenantMongoDatabaseProvider {

    /**
     *
     **/
    private static final Map<String, String> databaseIndexMap = new ConcurrentHashMap<>();

    /**
     * 获取租户数据库
     *
     * @return
     */
    public static String getDatabase() {
        String key = ThreadTenantUtil.getTenant();
        if (null == key) {
            return null;
        } else {
            //设置租户数据库
            setDatabase(getDefaultDbName() + "-" + key);
        }
        return databaseIndexMap.get(key);
    }

    /**
     * 获取mongo默认数据库
     *
     * @return
     */
    public static String getDefaultDbName() {
        return databaseIndexMap.get(TenantDataSourceProvider.DEFAULT_KEY);
    }

    /**
     * 设置mongo数据库
     *
     * @param database
     */
    public static void setDatabase(String database) {
        String key = ThreadTenantUtil.getTenant();
        if (null == key) {
            return;
        }
        databaseIndexMap.putIfAbsent(ThreadTenantUtil.getTenant(), database);
    }

    /**
     * 设置mongo数据库
     *
     * @param tenantCode
     * @param database
     */
    public static void setDatabase(String tenantCode, String database) {
        if (StringUtils.isBlank(tenantCode)) {
            return;
        }
        databaseIndexMap.put(tenantCode, database);
    }


    public static Map<String, String> getDatabaseIndexMap() {
        return databaseIndexMap;
    }

    public static Boolean checkDataBaseExist(String tenantCode) {
        return databaseIndexMap.containsKey(tenantCode);
    }
}