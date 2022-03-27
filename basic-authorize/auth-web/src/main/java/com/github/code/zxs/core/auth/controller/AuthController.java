package com.github.code.zxs.core.auth.controller;



















@RestController
@Slf4j
@Api("认证接口")
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfig jwtConfig;

    @ApiOperation("jwt模式下，获取公钥配置")
    @GetMapping("jwt/publicKey")
    public PublicKey getPublicKey() {
        return Optional.ofNullable(jwtConfig).map(JwtConfig::getPublicKey)
                .orElseThrow(() -> new ConfigNotInitException("Jwt配置未初始化"));
    }


    @ApiOperation("登录")
    @PostMapping("loginInfo")
    public LoginInfoDTO login(@RequestBody TokenUserDTO tokenUserDTO) {
        return authService.login(tokenUserDTO);
    }

    @ApiOperation("登出")
    @DeleteMapping("logout")
    public Boolean logout(@RequestBody List<String> tokens) {
        return authService.logout(tokens);
    }

    @ApiOperation("登出")
    @DeleteMapping("loginInfo")
    public Boolean logout(@RequestBody LoginInfoDTO loginInfo) {
        return authService.logout(loginInfo);
    }

    @ApiOperation("获取访问令牌中的用户信息")
    @ApiImplicitParam(name = "accessToken", value = "访问令牌")
    @GetMapping("tokenInfo")
    public TokenInfoDTO getTokenUserDTO(String accessToken) {
        return authService.getTokenInfoDTO(accessToken);
    }
}

