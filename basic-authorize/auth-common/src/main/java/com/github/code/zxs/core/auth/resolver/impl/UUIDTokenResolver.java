package com.github.code.zxs.core.auth.resolver.impl;








/**
 * UUID解析器
 */
@Component
@ConditionalOnBean(UUIDTokenConfig.class)
public class UUIDTokenResolver extends AbstractTokenResolver {
    @Autowired
    private AuthClient authClient;

    @Override
    protected TokenInfoDTO getTokenInfo(String accessToken) {
        return authClient.getTokenUserDTO(accessToken).assertSuccess();
    }
}
