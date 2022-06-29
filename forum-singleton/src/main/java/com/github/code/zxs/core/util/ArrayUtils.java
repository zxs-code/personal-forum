package com.github.code.zxs.core.util;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {
    /**
     * 包含所有（数据量小时使用）
     *
     * @param source
     * @param candidates
     * @return
     * @throws NullPointerException
     */
    public static boolean containsAll(Object[] source, Object[] candidates) {
        if (source.length < candidates.length)
            return false;
        for (Object c : candidates)
            if (!contains(source, c))
                return false;
        return true;
    }

    /**
     * 包含任意一个（数据量小时使用）
     *
     * @param source
     * @param candidates
     * @return
     * @throws NullPointerException
     */
    public static boolean containsAny(Object[] source, Object[] candidates) {
        for (Object c : candidates)
            if (contains(source, c))
                return true;
        return false;
    }


    public static void requireNonEmpty(Object[] elem) {
        if (ArrayUtils.isEmpty(elem))
            throw new IllegalArgumentException("数组不能为空");
    }

    public static void requireNonEmpty(Object[] elem, String message) {
        if (ArrayUtils.isEmpty(elem))
            throw new IllegalArgumentException(message);
    }

    @SafeVarargs
    public static <T> T[] asArray(T... elem) {
        return elem;
    }
}
