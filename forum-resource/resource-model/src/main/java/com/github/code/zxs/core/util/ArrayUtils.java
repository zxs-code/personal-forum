package com.github.code.zxs.core.util;

public class ArrayUtils {
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
            if (!org.apache.commons.lang3.ArrayUtils.contains(source, c))
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
            if (org.apache.commons.lang3.ArrayUtils.contains(source, c))
                return true;
        return false;
    }


}
