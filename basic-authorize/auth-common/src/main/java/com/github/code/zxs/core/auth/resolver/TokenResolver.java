package com.github.code.zxs.core.auth.resolver;



public interface TokenResolver {

    TokenInfoDTO resolve(String accessToken);

}
