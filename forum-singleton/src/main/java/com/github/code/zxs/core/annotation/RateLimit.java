package com.github.code.zxs.core.annotation;

import com.github.code.zxs.core.generator.base.KeyGenerator;
import com.github.code.zxs.core.model.enums.RateLimitStrategyEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 频率限制
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
//  和@Callable冲突 TODO
public @interface RateLimit {
    /**
     * key的前缀
     */
    String prefix() default "";

    /**
     * 默认为 方法名和方法参数组合, 使用spel表达式。
     * 如果配置了KeyGenerator，将忽略此属性
     */
    String key() default "";

    /**
     * key的构造器,使用时需要注入Spring容器
     */
    Class<? extends KeyGenerator> keyGenerator() default KeyGenerator.class;

    /**
     * 最大瞬间流量，默认为0
     */
    long maxBurst() default 0;

    /**
     * 周期内回复的令牌数量，平滑回复，默认回复数量为10
     */
    long countPerPeriod() default 10;

    /**
     * 周期，默认60
     */
    long period() default 60;

    /**
     * 周期时间单位，默认为秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 每次请求需要的令牌数量
     */
    long quantity() default 1;

    /**
     * 访问被拒绝时的提示消息
     *
     * @return
     */
    String message() default "";

    /**
     * 是否为固定窗口频率
     * @return
     */
    boolean fixed() default false;

    /**
     * 限流执行时机
     *
     * @return
     */
    RateLimitStrategyEnum strategy() default RateLimitStrategyEnum.BEFORE;
}
