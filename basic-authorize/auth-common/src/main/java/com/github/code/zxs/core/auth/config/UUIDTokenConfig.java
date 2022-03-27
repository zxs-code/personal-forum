package com.github.code.zxs.core.auth.config;











@Getter
@Setter
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "global.config.auth.token", name = "token-model", havingValue = "uuid", matchIfMissing = true)
@ConfigurationProperties(prefix = "global.config.auth.token.uuid")
public class UUIDTokenConfig {
    /**
     * token缓存key前缀
     */
    public static final String CACHE_TOKEN_PREFIX = "auth:uuid:";

    @PostConstruct
    public void init() {
        log.info("uuid-token配置加载完毕");
    }

    public static String getTokenCacheKey(String token) {
        return CACHE_TOKEN_PREFIX + token;
    }


}
