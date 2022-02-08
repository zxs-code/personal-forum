package com.code.zxs.auth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();


    @Nullable
    public static String serialize(Object obj) {

        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    @Nullable
    public static <T> T parse(String json, Class<T> tClass) {
        try {
            if (tClass.equals(String.class))
                return tClass.cast(json);
            return json == null ? null : mapper.readValue(json, tClass);
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    @Nullable
    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return json == null ? null : mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    @Nullable
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return json == null ? null : mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 解析带有泛型的类型
     * List<String> strings = JsonUtils.nativeRead("", new TypeReference<List<String>>() {});
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T parse(String json, TypeReference<T> type) {
        try {
            return json == null ? null : mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

}
