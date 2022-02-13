package com.code.zxs.auth.resolver.impl;

import com.code.zxs.auth.config.ConfigStaticReference;
import com.code.zxs.auth.config.JwtConfig;
import com.code.zxs.auth.dto.TokenUserDTO;
import com.code.zxs.auth.enums.TokenType;
import com.code.zxs.auth.resolver.TokenResolver;
import com.code.zxs.auth.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

/**
 * jwt解析器，无Key参数构造时，默认使用RSA加密算法
 */
@Slf4j
public class JwtResolver implements TokenResolver {
    private String token;
    private JwsHeader header;
    private Claims body;
    private Key key;

    /**
     * @param token
     * @param key     解析token的密钥
     * @param expired 是否允许token过期
     */
    public JwtResolver(String token, Key key, boolean expired) {
        log.info("初始化jwt解析器，传入token为{}", token);
        this.token = token;
        this.key = key;
        try {
            Jws<Claims> claimsJws = JwtUtils.parserToken(token, key);
            this.header = claimsJws.getHeader();
            this.body = claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            if (expired) {
                this.header = (JwsHeader) e.getHeader();
                this.body = e.getClaims();
            } else
                throw e;
        }
    }

    public JwtResolver(String token, Key key) {
        this(token, key, false);
    }

    /**
     * 无Key参数构造时，采用默认配置中的Key
     * @param token
     * @param expired 是否允许token过期
     */
    public JwtResolver(String token, boolean expired) {
        this(token, ConfigStaticReference.getJwtConfig().getPublicKey(),expired);
    }


    public JwtResolver(String token) {
        this(token,false);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Date getExpiration() {
        return body.getExpiration();
    }

    @Override
    public Date getIssueAt() {
        return body.getIssuedAt();
    }

    @Override
    public TokenUserDTO getUserInfo() {
        return JwtUtils.getInfoFromBody(body, JwtConfig.USER_INFO_KEY, TokenUserDTO.class);
    }

    @Override
    public TokenType getTokenType() {
        return JwtUtils.getInfoFromBody(body, JwtConfig.TOKEN_TYPE_KEY, TokenType.class);
    }

    public Key getKey() {
        return key;
    }
}
