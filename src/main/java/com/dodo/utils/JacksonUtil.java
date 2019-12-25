package com.dodo.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JacksonUtil {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Java对象转Json
    public static String toJackson(Object object) throws JsonProcessingException {
        return object == null ? null : OBJECT_MAPPER.writeValueAsString(object);
    }

    // Json转Java对象
    public static <T> T toObject(String jsonStr, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, clazz);
    }

    // Json转Java对象，类型安全
    public static <T> T toObject(String jsonStr, TypeReference<?> typeReference) throws IOException,
            JsonParseException, JsonMappingException {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, typeReference);
    }

    // Json转Java集合对象，类型安全，可使用toObject(String jsonStr, TypeReference<?> typeReference) 替代
    public static <T> T toCollectionObject(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses)
            throws IOException, JsonParseException, JsonMappingException {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, getCollectionType(collectionClass, elementClasses));
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
