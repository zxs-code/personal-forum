package com.github.code.zxs.auth.resolver;

import com.github.code.zxs.auth.dto.TokenInfoDTO;

public interface TokenResolver {

    TokenInfoDTO resolveAccessToken(String accessToken);

    TokenInfoDTO resolveRefreshToken(String RefreshToken);

}
