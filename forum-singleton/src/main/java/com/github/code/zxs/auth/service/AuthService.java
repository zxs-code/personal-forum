package com.github.code.zxs.auth.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.github.code.zxs.auth.model.bo.UserBO;
import com.github.code.zxs.auth.model.dto.UserRegisterDTO;
import com.github.code.zxs.auth.model.enums.AccountType;
import com.github.code.zxs.auth.model.enums.VerifyType;
import com.github.code.zxs.auth.model.vo.UserLoginDTO;

import java.util.concurrent.TimeUnit;

public interface AuthService {

    UserBO getUserBO(Long userId);

    void register(UserRegisterDTO userRegisterDTO);

    SaTokenInfo login(UserLoginDTO userLoginDTO);

    void logout();

    /**
     * 发送验证码
     *
     * @param accountType
     * @param verifyType
     * @param account
     * @param expire      有效时长，默认为 10,-1为不会失效
     * @param unit        时间单位，默认为分钟
     */
    void sendCaptcha(AccountType accountType, VerifyType verifyType, String account, Long expire, TimeUnit unit);

    void modifyPassword(AccountType accountType, VerifyType verifyType, String account, String password, String captcha);

    boolean existAccount(String account, AccountType accountType);

    boolean existUsername(String username);
}
