package com.code.zxs.auth.resolver.impl;

import com.code.zxs.auth.dto.TokenUserDTO;
import com.code.zxs.auth.resolver.TokenResolver;

import java.util.Date;

public class UUIDTokenResolver implements TokenResolver {
    private String token;
    public UUIDTokenResolver(String token){
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Date getExpiration() {
        return null;
    }

    @Override
    public Date getIssueAt() {
        return null;
    }

    @Override
    public TokenUserDTO getUserInfo() {
        return null;
    }
}
