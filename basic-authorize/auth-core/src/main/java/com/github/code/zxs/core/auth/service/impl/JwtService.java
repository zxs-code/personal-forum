package com.github.code.zxs.core.auth.service.impl;












@Service
@Slf4j
@ConditionalOnBean(JwtConfig.class)
public class JwtService extends AbstractTokenService {
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected String createToken(TokenInfoDTO tokenInfoDTO) {
        return JwtUtils.generateToken(tokenInfoDTO, tokenInfoDTO.getIssueTime(), tokenInfoDTO.getExpiration(), SignatureAlgorithm.RS256, jwtConfig.getPrivateKey());
    }

    @Override
    public boolean removeToken(List<String> tokens) {
        return false;
    }

    @Override
    protected TokenInfoDTO resolveToken(String token) {
        return JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey(), TokenInfoDTO.class);
    }
}
