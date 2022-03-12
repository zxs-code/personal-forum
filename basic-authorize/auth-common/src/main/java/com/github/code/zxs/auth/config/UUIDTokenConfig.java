package com.github.code.zxs.auth.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "global.config.auth.token", name = "token-model", havingValue = "uuid")
@ConfigurationProperties(prefix = "global.config.auth.token.uuid")
public class UUIDTokenConfig {
    /**
     * accessToken缓存key前缀
     */
    public static final String TOKEN_PREFIX = "auth:uuid:";

    @PostConstruct
    public void init() {
        log.info("uuid-token配置加载完毕");
    }

    public static String getTokenCacheKey(String token) {
        return TOKEN_PREFIX + token;
    }
}
