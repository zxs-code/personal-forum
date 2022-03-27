package com.github.code.zxs.core.user.service.impl;

















@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private RedisUtil redisUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Override
    public LoginInfoDTO login(TokenUserDTO tokenUserDTO) {
        TokenInfoDTO accessToken = tokenService.createAccessToken(tokenUserDTO);
        TokenInfoDTO refreshToken = tokenService.createRefreshToken(tokenUserDTO);

        LoginInfoDTO loginInfoDTO = LoginInfoDTO
                .builder()
                .accessToken(accessToken.getToken())
                .refreshToken((accessToken.getToken()))
                .userDTO(tokenUserDTO)
                .loginTime(refreshToken.getIssueTime())
                .loginTime(refreshToken.getExpiration())
                .build();
        //将登录信息放入缓存
        redisUtil.hPut(AuthConfig.getLoginInfoKey(tokenUserDTO.getId()), loginInfoDTO.getId(), loginInfoDTO);
        return loginInfoDTO;
    }


    @Override
    public void logout(String userId, String loginInfoId) {
        LoginInfoDTO loginInfoDTO = authService.getLoginInfo(userId, loginInfoId);
        //判断是否可以强制下线
        if (loginInfoDTO.getCanRemove()) {
            List<String> list = new ArrayList<>();
            list.add(loginInfoDTO.getAccessToken());
            list.add(loginInfoDTO.getRefreshToken());
            tokenService.removeToken(list);
        }
        redisUtil.hDelete(AuthConfig.getLoginInfoKey(userId), loginInfoId);
    }

    @Override
    public void logout(String userId) {
        List<LoginInfoDTO> infos = authService.getLoginInfo(userId);
        List<String> list = new ArrayList<>();
        infos.stream().filter(LoginInfoDTO::getCanRemove)
                .forEach(info -> {
                    list.add(info.getAccessToken());
                    list.add(info.getRefreshToken());
                });
        if (!CollectionUtils.isEmpty(list))
            tokenService.removeToken(list);
        redisUtil.delete(AuthConfig.getLoginInfoKey(userId));
    }

    @Override
    public List<LoginInfoDTO> getLoginInfo(String userId) {
        return redisUtil.hValues(AuthConfig.getLoginInfoKey(userId), LoginInfoDTO.class);
    }

    @Override
    public LoginInfoDTO getLoginInfo(String userId, String loginInfoId) {
        return redisUtil.hGet(AuthConfig.getLoginInfoKey(userId), loginInfoId, LoginInfoDTO.class);
    }

}
