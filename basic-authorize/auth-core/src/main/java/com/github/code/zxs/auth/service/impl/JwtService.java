package com.github.code.zxs.auth.service.impl;

import com.github.code.zxs.auth.config.JwtConfig;
import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.util.JwtUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@ConditionalOnBean(JwtConfig.class)
public class JwtService extends AbstractTokenService {
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public boolean removeToken(List<String> tokens) {
        return false;
    }

    @Override
    protected String createToken(TokenInfoDTO tokenInfoDTO) {
        return JwtUtils.generateToken(tokenInfoDTO, tokenInfoDTO.getIssueTime(), tokenInfoDTO.getExpiration(), SignatureAlgorithm.RS256, jwtConfig.getPrivateKey());
    }
}
