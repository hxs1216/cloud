
package com.biyao.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;

/**
 * 有序化JsonUtil
 *
 * @author: hxs
 * @create: 2020/4/13
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        // 反序列化时，忽略目标对象没有的属性。
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static <T> T stringToObject(String jsonString, Class<T> className) {
        if (null != jsonString) {
            if (!StringUtils.isEmpty(jsonString)) {
                try {
                    return mapper.readValue(jsonString, className);
                } catch (Exception var3) {
                    log.error("parse json to object failed, " + var3.getMessage());
                }
            }
            log.info("the message body is empty");
        }
        return null;
    }

    public static String objectToJsonString(Object object) {
        if (null != object) {
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException var2) {
                log.error("parse object to json failed, " + var2.getMessage());
            }
        }
        return null;
    }

    public static JsonNode stringToJsonNode(String jsonString) {
        if (null != jsonString) {
            if (!StringUtils.isEmpty(jsonString)) {
                try {
                    return mapper.readTree(jsonString);
                } catch (Exception var2) {
                    log.error("parse json to object failed, " + var2.getMessage());
                }
            }

            log.info("the message body is empty");
        }

        return null;
    }

    public static <T> Object stringToCollectionObject(String jsonString, Class collectionClass, Class<T> className) {
        if (null != jsonString) {
            if (!StringUtils.isEmpty(jsonString)) {
                try {
                    JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, new Class[]{className});
                    return mapper.readValue(jsonString, javaType);
                } catch (Exception var4) {
                    log.error("parse json to object failed, " + var4.getMessage());
                }
            }

            log.info("the message body is empty");
        }
        return null;
    }


}
