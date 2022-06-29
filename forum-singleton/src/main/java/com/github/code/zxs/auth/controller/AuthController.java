package com.github.code.zxs.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.github.code.zxs.auth.model.dto.UserRegisterDTO;
import com.github.code.zxs.auth.model.enums.AccountType;
import com.github.code.zxs.auth.model.enums.VerifyType;
import com.github.code.zxs.auth.model.vo.TokenInfoVO;
import com.github.code.zxs.auth.model.vo.UserLoginDTO;
import com.github.code.zxs.auth.service.AuthService;
import com.github.code.zxs.core.annotation.RateLimit;
import com.github.code.zxs.core.model.enums.RateLimitStrategyEnum;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping
    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    @PostMapping("login")
    @RateLimit(key = "#userLoginDTO.username", countPerPeriod = 1, period = 3, message = "3秒内只能登录一次")
    public TokenInfoVO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        SaTokenInfo tokenInfo = authService.login(userLoginDTO);
        return new TokenInfoVO(tokenInfo.getTokenName(), tokenInfo.getTokenValue());
    }

    @DeleteMapping("logout")
    public void logout() {
        authService.logout();
    }

    @ApiOperation("发送验证码")
    @PostMapping("captcha")
    @RateLimit(prefix = "auth", countPerPeriod = 1, period = 60, message = "60秒内只能发送一次验证码", strategy = RateLimitStrategyEnum.AFTER_RETURNING)
    public void sendCaptcha(
            @RequestParam AccountType accountType,
            @RequestParam VerifyType verifyType,
            @RequestParam String account) {
        authService.sendCaptcha(accountType, verifyType, account, 15L, TimeUnit.MINUTES);
    }


    @ApiOperation("修改密码")
    @PutMapping("password")
    @RateLimit(prefix = "auth", countPerPeriod = 15, period = 15, unit = TimeUnit.MINUTES, message = "请求频繁，请稍后再试", strategy = RateLimitStrategyEnum.AFTER_RETURNING)
    public void modifyPassword(
            @RequestParam AccountType accountType,
            @RequestParam VerifyType verifyType,
            @RequestParam String account,
            @RequestParam @Length(min = 6, max = 16, message = "密码长度在6到16位之间") String password,
            @RequestParam String captcha) {
        authService.modifyPassword(accountType, verifyType, account, password, captcha);
    }

    @PostMapping("register")
    public void register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        authService.register(userRegisterDTO);
    }
}
