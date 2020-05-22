package com.biyao.security.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author ziyan
 * @email zhengmengyan@taoqicar.com
 * @date 2018年12月18日
 */
public class EnumMap<K,V> {

    private Map<K, V> valueMap;

    EnumMap(Class<V> clazz, Function<V, K> function) {
        V[] enumConstants = clazz.getEnumConstants();
        if (enumConstants != null) {
            valueMap = new HashMap<>(enumConstants.length);
            for (V enumConstant : enumConstants) {
                valueMap.put(function.apply(enumConstant), enumConstant);
            }
        } else {
            valueMap = Collections.emptyMap();
        }
    }

    protected V fromValue(K value) {
        return valueMap.get(value);
    }
}
