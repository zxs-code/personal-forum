package com.github.code.zxs.core.auth.resolver.impl;














/**
 * jwt解析器，无Key参数构造时，默认使用RSA解密算法
 * <p>
 * {@link ConditionalOnBean} 可能有bean加载顺序问题 TODO
 */
@Slf4j
@Component
//@ConditionalOnExpression("!T(com.github.code.zxs.auth.enums.TokenModel).UUID.getModel().equals('${global.config.auth.token.token-model:jwt}') && !${global.config.auth.token.jwt.expired}")
//@ConditionalOnExpression("!'${global.config.auth.token.token-model:jwt}'.equals(T(com.github.code.zxs.auth.enums.TokenModel).UUID.getModel())&& !${global.config.auth.token.jwt.expired}")
@ConditionalOnBean(JwtConfig.class)
public class JwtResolver extends AbstractTokenResolver {

    @Autowired
    private AuthClient authClient;
    /**
     * 解析token的密钥
     */
    private PublicKey publicKey;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        if (publicKey == null) {
            try {
                FeignResult<PublicKey> result = authClient.getPublicKey();
                PublicKey publicKey = result.assertSuccess();
                this.publicKey = publicKey;
            } catch (Exception e) {
                log.error("初始化公钥失败", e);
                throw e;
            }
        }
        super.after();
    }

    public JwtResolver(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public JwtResolver() {
    }

    @Override
    protected TokenInfoDTO getTokenInfo(String token) {
        return JwtUtils.getInfoFromToken(token, publicKey, TokenInfoDTO.class);
    }
}

