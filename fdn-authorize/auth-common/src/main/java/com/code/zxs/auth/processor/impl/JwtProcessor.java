package com.code.zxs.auth.processor.impl;

import com.code.zxs.auth.config.JwtConfig;
import com.code.zxs.auth.processor.TokenProcessor;
import com.code.zxs.auth.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProcessor implements TokenProcessor {
    @Autowired
    private JwtConfig jwtConfig;

    public JwtProcessor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    @Override
    public String generateToken(Object info) {
        return JwtUtils.generateToken(info, jwtConfig.getPrivateKey(), jwtConfig.getTokenExpire(), jwtConfig.getUnit());
    }

    @Override
    public String generateToken(Object info, Date expiration) {
        return JwtUtils.generateToken(info, jwtConfig.getPrivateKey(), expiration);
    }

    @Override
    public String generateToken(Object info, Integer expire, ChronoUnit unit) {
        return JwtUtils.generateToken(info, jwtConfig.getPrivateKey(), expire, unit);
    }

    @Override
    public String generateToken(Map<String, Object> infoMap) {
        return JwtUtils.generateToken(infoMap, jwtConfig.getPrivateKey(), jwtConfig.getTokenExpire(), jwtConfig.getUnit());
    }

    @Override
    public String generateToken(Map<String, Object> infoMap, Date expiration) {
        return JwtUtils.generateToken(infoMap, jwtConfig.getPrivateKey(), expiration);

    }

    @Override
    public String generateToken(Map<String, Object> infoMap, Integer expire, ChronoUnit unit) {
        return JwtUtils.generateToken(infoMap, jwtConfig.getPrivateKey(), expire, unit);
    }

    @Override
    public <T> T getInfoFromToken(String token, Class<T> type) {
        try {
            return JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey(), type);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get(JwtUtils.DEFAULT_CLAIM_NAME, type);
        }
    }


    @Override
    public <T> T getInfoFromToken(String token, String infoKey, Class<T> type) {
        try {
            return JwtUtils.getInfoFromToken(token, infoKey, jwtConfig.getPublicKey(), type);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get(infoKey, type);
        }
    }

    @Override
    public Date getTokenExpiration(String token) {
        try {
            return JwtUtils.getExpirationFromToken(token, jwtConfig.getPublicKey());
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }

    public static void main(String[] args) {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.init();
        String s = JwtUtils.generateToken("123", jwtConfig.getPrivateKey(), -100, ChronoUnit.DAYS);
        System.out.println(JwtUtils.getExpirationFromToken(s, jwtConfig.getPublicKey()));
        System.out.println(JwtUtils.getInfoFromToken(s, jwtConfig.getPublicKey(), String.class));
    }
}
