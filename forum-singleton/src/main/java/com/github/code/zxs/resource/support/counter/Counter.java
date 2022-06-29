package com.github.code.zxs.resource.support.counter;

public interface Counter {
    /**
     * 获取key对应的计数
     *
     * @param key
     * @return
     */
    long getCount(String key);

    /**
     * 计数加 delta
     *
     * @param key
     * @param delta
     * @return 增加后的值
     */
    long incr(String key, long delta);

    /**
     * 计数加 默认值
     *
     * @param key
     * @return 增加后的值
     */
    long incr(String key);

    /**
     * 计数减 delta
     *
     * @param key
     * @param delta
     * @return 减少后的值
     */
    long decr(String key, long delta);

    /**
     * 计数减 默认值
     *
     * @param key
     * @return 减少后的值
     */
    long decr(String key);

    /**
     * 获取旧值并加 默认值
     *
     * @param key
     * @return
     */
    long getAndIncr(String key);

    /**
     * 获取旧值并加 delta
     *
     * @param key
     * @param delta
     * @return
     */
    long getAndIncr(String key, long delta);

    /**
     * 获取旧值并减 默认值
     *
     * @param key
     * @return
     */
    long getAndDecr(String key);

    /**
     * 获取旧值并减 delta
     *
     * @param key
     * @param delta
     * @return
     */
    long getAndDecr(String key, long delta);
}
