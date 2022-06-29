package com.github.code.zxs.core.util;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanUtils extends org.springframework.beans.BeanUtils {


    public static <T> T copy(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static Map<String, Class<?>> resolveFieldMap(Class<?> cls) {
        HashMap<String, Class<?>> map = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> type = field.getType();
            map.put(fieldName, type);
        }
        return map;
    }


    public static <T> Set<Class<? extends T>> getSubClasses(String packagePrefix, Class<T> target) {
        // 判断class对象是否是一个接口
        Reflections reflections = new Reflections(packagePrefix);
        // 获取所有类,然后判断是否是 target 接口的实现类
        return reflections.getSubTypesOf(target);
    }
}
