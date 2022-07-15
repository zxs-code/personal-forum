package com.github.code.zxs.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    String redisHost;

    @Value("${spring.redis.port}")
    String redisPort;

    @Value("${spring.redis.password}")
    String redisPass;

    @Value("${spring.redis.database}")
    String database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(new StringCodec());
        config.useSingleServer() // 使用单机模式
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setDatabase(Integer.parseInt(database))
                .setKeepAlive(true)
                // 设置1秒钟ping一次来维持连接
                .setPingConnectionInterval(1000)
                .setPassword(redisPass);
        return Redisson.create(config);
    }
}

