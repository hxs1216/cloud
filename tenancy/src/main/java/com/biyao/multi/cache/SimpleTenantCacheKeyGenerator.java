package com.biyao.multi.cache;

import com.biyao.entities.TenantCacheKey;
import com.biyao.util.ThreadTenantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 功能描述 多租户缓存生成器
 *
 * @author hxs
 * @date 2020/5/15
 */
public class SimpleTenantCacheKeyGenerator implements KeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(SimpleTenantCacheKeyGenerator.class);

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        Object result = new TenantCacheKey(ThreadTenantUtil.getTenant(), objects);
        log.debug("cacheKey {}", result);
        return result;
    }

}