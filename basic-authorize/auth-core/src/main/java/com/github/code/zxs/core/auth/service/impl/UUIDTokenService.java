package com.github.code.zxs.core.auth.service.impl;














@Service
@Slf4j
@ConditionalOnBean(UUIDTokenConfig.class)
public class UUIDTokenService extends AbstractTokenService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected String createToken(TokenInfoDTO tokenInfoDTO) {
        String token = UUIDUtils.randomCompactUUID();
        Duration duration = Duration.between(tokenInfoDTO.getIssueTime().toInstant(), tokenInfoDTO.getExpiration().toInstant());
        redisUtil.setEx(UUIDTokenConfig.getTokenCacheKey(token), tokenInfoDTO, duration);
        return token;
    }

    @Override
    public boolean removeToken(List<String> tokens) {
        List<String> list = tokens.stream().map(UUIDTokenConfig::getTokenCacheKey).collect(Collectors.toList());
        redisUtil.delete(list);
        return true;
    }

    @Override
    protected TokenInfoDTO resolveToken(String token) {
        String cacheKey = UUIDTokenConfig.getTokenCacheKey(token);
        return redisUtil.get(cacheKey, TokenInfoDTO.class);
        redisUtil.sAdd()
    }
}
