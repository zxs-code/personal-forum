package com.github.code.zxs.core.auth.config;






/**
 * 线程池配置属性类
 */
@Data
@Configuration
@ConditionalOnProperty(prefix = "global.config.core.task.pool", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "global.config.core.task.pool")
public class TaskThreadPoolConfig {
    /**
     * 是否覆盖默认异步线程池
     */
    private boolean enable;
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maxPoolSize;
    /**
     * 线程存活时间
     */
    private int keepAliveSeconds;
    /**
     * 等待队列最大任务数
     */
    private int queueCapacity;
}