package com.github.code.zxs.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    /**
     * Jackson2JsonRedisSerializer 序列化和反序列化效率高
     */
    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //开启事务支持
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper om) {
        //POJO无public的属性或方法时，不报错
        ObjectMapper copy = om.copy();
        copy.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // null值字段不显示
        copy.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        copy.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 序列化JSON串时，在值上打印出对象类型
//         copy.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        copy.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        // 替换上方 过期的enableDefaultTyping
        copy.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(copy);

        // 解决jackson2无法反序列化LocalDateTime的问题
//        copy.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        copy.registerModule(new ZxlJavaTimeModule());

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 默认 7天过期
//                .entryTtl(Duration.ofDays(7))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

}