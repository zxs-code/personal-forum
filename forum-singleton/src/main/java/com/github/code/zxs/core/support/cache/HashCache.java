package com.github.code.zxs.core.support.cache;

import com.github.code.zxs.core.util.*;
import com.github.code.zxs.resource.model.entity.PostsData;
import lombok.Setter;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class HashCache<K, V> {
    private static final String SCAN = "scan";

    private final String cacheName;
    private final String keyPrefix;
    private final RedissonClient redissonClient;
    private final Class<V> vClass;
    private final Map<String, Class<?>> vClassFieldMap;
    private static final Map<Class<?>, Map<String, Class<?>>> CLASS_PROPERTY_MAP;
    private long afterWriteExpire = -1;
    private TimeUnit unit = TimeUnit.SECONDS;

    static {
        CLASS_PROPERTY_MAP = new ConcurrentHashMap<>();
    }

    public HashCache(String cacheName, Class<V> vClass) {
        this.cacheName = cacheName;
        this.keyPrefix = SCAN + ":" + cacheName;
        this.redissonClient = SpringContextUtils.getBean(RedissonClient.class);
        this.vClass = vClass;
        CLASS_PROPERTY_MAP.computeIfAbsent(vClass, BeanUtils::resolveFieldMap);
        vClassFieldMap = CLASS_PROPERTY_MAP.get(vClass);
    }

    public void asyncIncr(K key, String filedName, double delta) {
        requireNumberType(filedName);
        increment(key, filedName, delta, true);
    }

    public void incr(K key, String filedName, double delta) {
        requireNumberType(filedName);
        increment(key, filedName, delta, false);
    }

    public void asyncIncr(K key, Map<String, Double> deltaMap) {
        deltaMap.forEach((fieldName, delta) -> asyncIncr(key, fieldName, delta));
    }

    public void incr(K key, Map<String, Double> deltaMap) {
        deltaMap.forEach((fieldName, delta) -> incr(key, fieldName, delta));
    }

    public void asyncPut(K key, String fieldName, Object fieldValue) {
        checkType(fieldName, fieldValue);
        put(key, fieldName, fieldValue, true);
    }

    public void put(K key, String fieldName, Object fieldValue) {
        checkType(fieldName, fieldValue);
        put(key, fieldName, fieldValue, false);
    }

    public void asyncPutAll(K key, V value) {
        Map<String, Object> beanMap = JsonUtils.toMap(value);
        putAll(key, beanMap, true);
    }

    public void putAll(K key, V value) {
        Map<String, Object> beanMap = JsonUtils.toMap(value);
        putAll(key, beanMap, false);
    }

    public void asyncPutAll(K key, Map<String, Object> beanMap) {
        beanMap.forEach(this::checkType);
        putAll(key, beanMap, true);
    }

    public void putAll(K key, Map<String, Object> beanMap) {
        beanMap.forEach(this::checkType);
        putAll(key, beanMap, false);
    }

    public void delete(K key) {
        String cacheKey = getCacheKey(key);
        RedisUtils.delete(cacheKey);
    }

    public void delete(K key, String... fieldNames) {
        String cacheKey = getCacheKey(key);
        RedisUtils.hDelete(cacheKey, fieldNames);
    }

    /**
     * 使用分布式锁防止缓存穿透
     *
     * @param key
     * @param function
     * @return
     */
    public V computeIfAbsent(K key, Function<K, V> function) {
        V v = get(key);
        if (v == null) {
            RLock lock = redissonClient.getLock(getLockKey(key));
            try {
                lock.lock();
                if ((v = get(key)) != null) {
                    return v;
                }
                v = function.apply(key);
                putAll(key, v);
            } finally {
                lock.unlock();
            }
        }
        return v;
    }

    public List<PostsData> getAll() {
        Set<String> keys = RedisUtils.scan(keyPrefix + "*");
        List<Map<String, Object>> maps = RedisUtils.hMultiEntries(new ArrayList<>(keys));
        List<PostsData> dataList = maps.stream().map(map -> JsonUtils.parse(map, PostsData.class)).collect(Collectors.toList());
        return dataList;
    }

    public V get(K key) {
        Map<String, Object> entries = RedisUtils.hEntries(getCacheKey(key));
        return JsonUtils.parse(entries, vClass);
    }

    public V get(K key, String... fieldName) {
        Arrays.stream(fieldName).forEach(this::checkField);
        List<Object> values = RedisUtils.hMultiGet(getCacheKey(key), CollectionUtils.asList(fieldName));
        Map<String, Object> entries = new HashMap<>();
        for (int i = 0; i < fieldName.length; i++) {
            entries.put(fieldName[i], values.get(i));
        }
        return JsonUtils.parse(entries, vClass);
    }

    private void putAll(K key, Map<String, Object> beanMap, boolean async) {
        String cacheKey = getCacheKey(key);
        RMap<String, Object> map = redissonClient.getMap(cacheKey);
        if (async)
            map.putAllAsync(beanMap);
        else
            map.putAll(beanMap);
        renewExpireAfterWrite(key, async);
    }

    private void put(K key, String fieldName, Object fieldValue, boolean async) {
        String cacheKey = getCacheKey(key);
        RMap<String, Object> map = redissonClient.getMap(cacheKey);
        if (async)
            map.fastPutAsync(fieldName, fieldValue);
        else
            map.fastPut(fieldName, fieldValue);
        renewExpireAfterWrite(key, async);
    }


    private void increment(K key, String fieldName, double delta, boolean async) {
        String cacheKey = getCacheKey(key);
        RMap<String, Object> map = redissonClient.getMap(cacheKey);
        if (async)
            map.addAndGetAsync(fieldName, delta);
        else
            map.addAndGet(fieldName, delta);
        renewExpireAfterWrite(key, async);
    }

    private void renewExpireAfterWrite(K key, boolean async) {
        String cacheKey = getCacheKey(key);
        if (afterWriteExpire > 0) {
            RMap<Object, Object> map = redissonClient.getMap(cacheKey);
            if (async)
                map.expireAsync(afterWriteExpire, unit);
            else
                map.expire(afterWriteExpire, unit);
        }
    }

    private String getCacheKey(K key) {
        return keyPrefix + ":" + key;
    }

    private String getLockKey(K key) {
        return keyPrefix + ":lock:" + key;
    }

    private void checkField(String fieldName) {
        if (!containsField(fieldName))
            throw new IllegalArgumentException(StringUtils.format("{}不存在{}属性", vClass, fieldName));
    }

    private boolean containsField(String fieldName) {
        return vClassFieldMap.containsKey(fieldName);
    }

    private void requireNumberType(String fieldName) {
        Class<?> fieldType = vClassFieldMap.get(fieldName);
        Assert.isTrue(fieldType != null && Number.class.isAssignableFrom(fieldType),
                StringUtils.format("只能自增数字类型的属性，fieldType:{}", fieldType));
    }

    private void checkType(String fieldName, Object value) {
        Class<?> fieldType = vClassFieldMap.get(fieldName);
        Class<?> valueType = value.getClass();
        Assert.isTrue(fieldType == valueType,
                StringUtils.format("值类型{}与属性{}类型{}不匹配", valueType, fieldName, fieldType));
    }
}
