package com.code.zxs.auth.resolver;

import com.code.zxs.auth.dto.TokenUserDTO;
import com.code.zxs.auth.enums.TokenType;

import java.util.Date;

public interface TokenResolver {
    String getToken();

    Date getExpiration();

    Date getIssueAt();

    TokenUserDTO getUserInfo();

    default boolean isAccessToken(){
        return getTokenType() == TokenType.ACCESS_TOKEN;
    }

    default boolean isRefreshToken(){
        return getTokenType() == TokenType.REFRESH_TOKEN;
    }

    default TokenType getTokenType() {
        return null;
    }

}
