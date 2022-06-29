package com.github.code.zxs.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
@Slf4j
public class RedisUtils {
    private static final int LIMIT = 1000;

    private static RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> template) {
        redisTemplate = template;
    }


    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static boolean expire(String key, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.asList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存获取
     *
     * @param keys 键
     * @return 值
     */
    public static List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 递增 适用场景： https://blog.csdn.net/y_y_y_k_k_k_k/article/details/79218254 高并发生成订单号，秒杀类的业务逻辑等。。
     *
     * @param key 键
     * @return
     */
    public static long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     * @return
     */
    public static long decr(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key 键 不能为null
     * @return 值
     */
    public static Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * HashGet
     *
     * @param key 键 不能为null
     * @return 值
     */
    public static Map<String, Object> entries(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Object k : entries.keySet()) {
            if (!(k instanceof String))
                throw new IllegalArgumentException("redis的hash的二级key不是字符串");
            result.put((String) k, entries.get(k));
        }
        return result;
    }

    /**
     * HashGet
     *
     * @param key   键 不能为null
     * @param items 项 必须为String
     * @return 值
     */
    public static List<Object> hMultiGet(String key, Collection<String> items) {
        List<Object> hashKey = items.stream().map(Object.class::cast).collect(Collectors.toList());
        return redisTemplate.opsForHash().multiGet(key, hashKey);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<String, Object> hEntries(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
//        Map<String, Object> result = entries.entrySet().stream().collect(Collectors.toMap(entry -> (String) entry.getKey(), Map.Entry::getValue));
        return (Map<String, Object>) (Map) entries;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @return 对应的多个键值
     */
    public static List<Map<String, Object>> hMultiEntries(List<String> keys) {
        List<List<String>> lists = CollectionUtils.subList(keys, LIMIT);
        List<Map<String, Object>> result = new ArrayList<>();
        for (List<String> subList : lists) {
            List<Object> list = redisTemplate.executePipelined(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    subList.forEach(key -> operations.opsForHash().entries(key));
                    return null;
                }
            });
            result.addAll(list.stream().map(m -> (Map<String, Object>) m).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * 获取hashKey对应的所有键
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取hashKey对应的所有值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }


    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hPutAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hPut(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hPut(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hDelete(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, Object item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public static Double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key 键
     * @return
     */
    public static void hIncr(String key, Map<String, Double> deltaMap) {
        RedisUtils.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                for (Map.Entry<String, Double> entry : deltaMap.entrySet()) {
                    ops.opsForHash().increment(key, entry.getKey(), entry.getValue());
                }
                return null;
            }
        });
    }

    /**
     * hash的大小
     *
     * @param key 键
     * @return
     */
    public static Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }


    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public static double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    // ============================zset=============================


    public static Boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    public static Long zAdd(String key, Set<TypedTuple<Object>> tuples) {
        try {
            return redisTemplate.opsForZSet().add(key, tuples);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    public static Double zIncrementScore(String key, Object value, double delta) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, delta);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }


    /**
     * 分数相同时，按时间戳排序，最大时间 5138-11-16 17:46:39
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static Boolean zAddWithDate(String key, Object value, long score) {
        try {
            double time = System.currentTimeMillis();
            long divisor = 100000000000000L;
            if (time >= divisor)
                throw new IllegalArgumentException("时间戳超出范围");
            double newScore = score + time / divisor;
            return redisTemplate.opsForZSet().add(key, value, newScore);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 分数相同时，按时间戳排序，最大时间 5138-11-16 17:46:39
     *
     * @param key
     * @param value
     * @param score
     * @return
     */

    public static Boolean zAddWithDate(String key, Object value, int score, Date date) {
        try {
            double time = date.getTime();
            long divisor = 100000000000000L;
            if (time >= divisor)
                throw new IllegalArgumentException("时间戳超出范围");
            double newScore = score + time / divisor;
            return redisTemplate.opsForZSet().add(key, value, newScore);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param
     * @return
     */
    public static List<Long> zMultiSize(List<String> keys) {
        try {
            return CollectionUtils.castToList(redisTemplate.executePipelined(new SessionCallback() {
                @Override
                public Object execute(RedisOperations ops) throws DataAccessException {
                    for (String key : keys)
                        ops.opsForZSet().size(key);
                    return null;
                }
            }), Long.class);
        } catch (Exception e) {
            log.error(keys.toString(), e);
            return CollectionUtils.emptyList();
        }
    }

    /**
     * zset范围查找
     *
     * @param key 键
     * @return
     */
    public static Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset范围查找
     *
     * @param key 键
     * @return
     */
    public static Set<Object> zRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset范围查找
     *
     * @param key 键
     * @return
     */
    public static Set<Object> zRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset范围查找
     *
     * @param key 键
     * @return
     */
    public static Long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset范围查找
     *
     * @param key 键
     * @return
     */
    public static Double zScore(String key, Object o) {
        try {
            return redisTemplate.opsForZSet().score(key, o);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset反向范围查找
     *
     * @param key 键
     * @return
     */
    public static Set<Object> zReverseRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset反向范围查找,包括分数
     *
     * @param key 键
     * @return
     */
    public static Set<TypedTuple<Object>> zReverseRangeWithScore(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset反向范围查找,包括分数
     *
     * @param key 键
     * @return
     */
    public static Set<Object> zReverseRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * zset范围查找,包括分数
     *
     * @param key 键
     * @return
     */
    public static Set<TypedTuple<Object>> zRangeWithScore(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static Long zRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForZSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始 0 是第一个元素
     * @param end   结束 -1代表所有值
     * @return
     * @取出来的元素 总数 end-start+1
     */
    public static List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }


    public static List<Object> executePipelined(SessionCallback<?> sessionCallback) {
        return redisTemplate.executePipelined(sessionCallback);
    }


    public static Boolean setBit(String key, long offset, boolean value) {
        try {
            return redisTemplate.opsForValue().setBit(key, offset, value);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    public static Boolean getBit(String key, long offset) {
        try {
            return redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    public static Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }

    public static Long bitCount(String key, int start, int end) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes(), start, end));
    }

    public static Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        byte[][] bytes = new byte[desKey.length][];
        for (int i = 0; i < desKey.length; i++) {
            bytes[i] = desKey[i].getBytes();
        }
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
    }

    /**
     * 使用后，连接会与当前线程绑定，并且不会解绑，
     * 导致该线程以事务方式执行代码中的非事务命令，
     * 导致 {@link NullPointerException},
     * 使用 {@link SessionCallback} 代替
     */
    @Deprecated
    public static void multi() {
        redisTemplate.multi();
    }

    /**
     * {@link RedisUtils#multi()}
     *
     * @return
     */
    @Deprecated
    public static List<Object> exec() {
        return redisTemplate.exec();
    }

    public static <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    /**
     * {@link RedisUtils#multi()}
     *
     * @return
     */
    @Deprecated
    public static void discard() {
        redisTemplate.discard();
    }

    /**
     * 支持单机、主从、哨兵、集群
     *
     * @param matchKey 要匹配的key的模糊表达式，例如 hpfm:lov:*
     * @return 所匹配到的key的Set集合
     */
    public static Set<String> scan(String matchKey) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(matchKey).count(LIMIT).build());
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
            return keys;
        });
    }


    public static String getSimpleCacheKey(Object... elements) {
        return StringUtils.join(elements, ':');
    }

}

