package com.github.code.zxs.auth.resolver.impl;

import com.github.code.zxs.auth.config.JwtConfig;
import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.enums.TokenType;
import com.github.code.zxs.auth.util.JwtUtils;
import com.github.code.zxs.core.exception.ConfigNotInitException;
import com.github.code.zxs.core.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

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
    private JwtConfig jwtConfig;
    /**
     * 解析token的密钥
     */
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        super.init();
        if (jwtConfig == null)
            try {
                jwtConfig = SpringContextUtils.getBean(JwtConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("容器中不存在该配置 {}", "jwtConfig");
                throw new ConfigNotInitException("Jwt配置没有初始化，请创建一个 JwtConfig bean对象");
            }
        this.publicKey = jwtConfig.getPublicKey();
        super.after();
    }

    public JwtResolver(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public JwtResolver() {
    }

    @Override
    protected TokenInfoDTO getTokenInfo(String token, TokenType tokenType) {
        return JwtUtils.getInfoFromToken(token, publicKey, TokenInfoDTO.class);
    }
}

