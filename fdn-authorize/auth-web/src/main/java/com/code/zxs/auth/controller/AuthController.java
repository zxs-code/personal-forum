package com.code.zxs.auth.controller;

import com.code.zxs.auth.config.JwtConfig;
import com.code.zxs.auth.dto.TokenUserDTO;
import com.code.zxs.auth.enums.TokenType;
import com.code.zxs.auth.resolver.impl.JwtResolver;
import com.code.zxs.auth.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    JwtConfig jwtConfig;

    @GetMapping("{test}")
    public Object test(@PathVariable String test) {
        Map<String, Object> claimsMap = new HashMap<>();
        TokenUserDTO tokenUserDTO = new TokenUserDTO();
        tokenUserDTO.setId("1234");
        tokenUserDTO.setCreateTime(new Date());
        tokenUserDTO.setUsername("小明");

        claimsMap.put(JwtConfig.USER_INFO_KEY,tokenUserDTO);
        claimsMap.put(JwtConfig.TOKEN_TYPE_KEY, TokenType.ACCESS_TOKEN);
        String token = JwtUtils.generateToken(claimsMap, jwtConfig.getPrivateKey(), -100, ChronoUnit.MINUTES);
        JwtResolver resolver = new JwtResolver(token,true);
        System.out.println(resolver.getKey());
        return resolver.getUserInfo();
    }
}
