package com.github.code.zxs.core.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class CacheUtils {
    public static <K, V> Cache<K, V> getSimpleLocalCache(int maximumSize, Duration expireAfterAccess) {
        return Caffeine
                .newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccess)
                .weakKeys()
                .weakValues()
                .build();
    }
}
