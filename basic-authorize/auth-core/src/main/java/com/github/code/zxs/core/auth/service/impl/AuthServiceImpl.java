package com.github.code.zxs.core.auth.service.impl;













@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public LoginInfoDTO login(TokenUserDTO tokenUserDTO) {
        TokenInfoDTO accessToken = tokenService.createAccessToken(tokenUserDTO);
        TokenInfoDTO refreshToken = tokenService.createRefreshToken(tokenUserDTO);

        return LoginInfoDTO
                .builder()
                .accessToken(accessToken.getToken())
                .refreshToken((accessToken.getToken()))
                .userDTO(tokenUserDTO)
                .loginTime(refreshToken.getIssueTime())
                .loginTime(refreshToken.getExpiration())
                .build();
    }

    @Override
    public Boolean logout(String token) {
        return authService.logout(Collections.singletonList(token));
    }

    @Override
    public Boolean logout(LoginInfoDTO loginInfoDTO) {
        return authService.logout(loginInfoDTO.getToken());
    }

    @Override
    public Boolean logout(List<String> token) {
        return tokenService.removeToken(token);
    }


    @Override
    public TokenInfoDTO getTokenInfoDTO(String accessToken) {
        return tokenService.getTokenInfo(accessToken);
    }
}
