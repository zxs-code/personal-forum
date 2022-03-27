package com.github.code.zxs.core.user.controller.admin;




















@RestController
@Slf4j
@Api("认证接口")
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private JwtConfig jwtConfig;

    @ApiOperation("jwt模式下，获取公钥配置")
    @GetMapping("jwt/publicKey")
    public PublicKey getPublicKey() {
        return Optional.ofNullable(jwtConfig).map(JwtConfig::getPublicKey)
                .orElseThrow(() -> new ConfigNotInitException("Jwt配置未初始化"));
    }


    @ApiOperation("登录")
    @PostMapping("login")
    public LoginInfoDTO login(@RequestBody TokenUserDTO tokenUserDTO) {
        return authService.login(tokenUserDTO);
    }

    @ApiOperation("登出单台设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "loginInfoId", value = "登录信息id")
    })
    @DeleteMapping("logout/{userId}/{loginInfoId}")
    public void logout(@PathVariable String userId, @PathVariable String loginInfoId) {
        authService.logout(userId, loginInfoId);
    }

    @ApiOperation("登出单台设备")
    @ApiImplicitParam(name = "userId", value = "用户id")
    @DeleteMapping("logout/{userId}")
    public void logout(@PathVariable String userId) {
        authService.logout(userId);
    }


    @ApiOperation("获取用户的所有登录信息")
    @ApiImplicitParam(name = "userId", value = "用户id")
    @GetMapping("loginInfo/{userId}")
    public List<LoginInfoDTO> getLoginInfo(@PathVariable String userId) {
        return authService.getLoginInfo(userId);
    }

    @ApiOperation("获取用户的某条登录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "loginInfoId", value = "登录信息id")
    })
    @GetMapping("loginInfo/{userId}/{loginInfoId}")
    public LoginInfoDTO getLoginInfo(@PathVariable String userId, @PathVariable String loginInfoId) {
        return authService.getLoginInfo(userId, loginInfoId);
    }


    @ApiOperation("获取访问令牌中的用户信息")
    @ApiImplicitParam(name = "loginInfoId", value = "登录信息id")
    @GetMapping("tokenInfo")
    public TokenInfoDTO getTokenUserDTO(String accessToken) {
        return tokenService.getTokenInfo(accessToken);
    }
}

