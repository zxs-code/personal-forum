package com.github.code.zxs.core.util;

import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils extends CollectionUtil {

    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }

    public static <T> Set<T> castToSet(Collection<?> collection, Class<T> c) {
        return collection.stream().map(c::cast).collect(Collectors.toSet());
    }

    public static <T> List<T> castToList(Collection<?> collection, Class<T> c) {
        return collection.stream().map(c::cast).collect(Collectors.toList());
    }

    public static List<Long> intToLong(Collection<?> collection) {
        return collection.stream().map(e -> {
            int num = (Integer) e;
            return (long) num;
        }).collect(Collectors.toList());
    }

    public static List<Long> intToLongDefaultZero(Collection<?> collection) {
        return collection.stream().map(e -> {
            if (Objects.isNull(e))
                return 0L;
            int num = (Integer) e;
            return (long) num;
        }).collect(Collectors.toList());
    }

    public static <T> List<List<T>> subList(List<T> list, int subSize) {
        if (list == null)
            return null;
        List<List<T>> result = new ArrayList<>();
        int i = 0;
        while (i < list.size()) {
            int toIndex = Math.min(i + subSize, list.size());
            result.add(list.subList(i, toIndex));
            i = toIndex;
        }
        return result;
    }

    public static <T> List<T> asList(T... elem) {
        return Arrays.stream(elem).collect(Collectors.toList());
    }

    public static <T> void requireNonEmpty(List<T> list) {
        if (isEmpty(list))
            throw new IllegalArgumentException("数组不能为空");
    }

    public static <T> void requireNonEmpty(List<T> list, String message) {
        if (isEmpty(list))
            throw new IllegalArgumentException(message);
    }

}
