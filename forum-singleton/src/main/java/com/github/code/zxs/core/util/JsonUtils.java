package com.github.code.zxs.core.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class JsonUtils {

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    public void init() {
        mapper1 = mapper.copy();
        mapper1.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        mapper2 = mapper.copy();
        mapper2.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        mapper2.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        mapper3 = mapper.copy();
        mapper3.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper3.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    private static ObjectMapper mapper1 = new ObjectMapper();
    private static ObjectMapper mapper2 = new ObjectMapper();
    private static ObjectMapper mapper3 = new ObjectMapper();

    @Nullable
    public static String serialize(Object obj) {

        if (obj == null) {
            return null;
        }
//        if (obj.getClass() == String.class) {
//            return (String) obj;
//        }
        try {
            return mapper1.writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <T> T parse(String json, Class<T> tClass) {
        try {
//            if (tClass.equals(String.class))
//                return tClass.cast(json);
            return json == null ? null : mapper1.readValue(json, tClass);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * 解析属性名为下划线形式的json
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T parseCanal(String json, TypeReference<T> type) {
        try {
//            if (tClass.equals(String.class))
//                return tClass.cast(json);
            return json == null ? null : mapper3.readValue(json, type);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <T> T parse(Object fromValue, Class<T> tClass) {
        try {
//            if (tClass.equals(String.class))
//                return tClass.cast(fromValue);
            return fromValue == null ? null : mapper1.convertValue(fromValue, tClass);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <T> T parse(Object fromValue, TypeReference<T> type) {
        try {
//            if (tClass.equals(String.class))
//                return tClass.cast(fromValue);
            return fromValue == null ? null : mapper1.convertValue(fromValue, type);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <T> T parseCanal(Object fromValue, Class<T> tClass) {
        try {
            return fromValue == null ? null : mapper3.convertValue(fromValue, tClass);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    //    @Nullable
    public static Map<String, Object> toMap(Object object) {
        return mapper1.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, Object> toMapWithNullAndUnderline(Object object) {
        return mapper2.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    @Nullable
    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return json == null ? null : mapper1.readValue(json, mapper1.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <E> Set<E> parseSet(String json, Class<E> eClass) {
        try {
            return json == null ? null : mapper1.readValue(json, mapper1.getTypeFactory().constructCollectionType(Set.class, eClass));
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Nullable
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return json == null ? null : mapper1.readValue(json, mapper1.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (Exception e) {
            throw new JsonParseException(e);
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
            return json == null ? null : mapper1.readValue(json, type);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

}
