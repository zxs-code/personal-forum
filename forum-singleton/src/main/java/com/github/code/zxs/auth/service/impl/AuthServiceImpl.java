package com.github.code.zxs.auth.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.code.zxs.auth.converter.UserConverter;
import com.github.code.zxs.auth.mapper.UserMapper;
import com.github.code.zxs.auth.model.bo.UserBO;
import com.github.code.zxs.auth.model.dto.UserRegisterDTO;
import com.github.code.zxs.auth.model.entity.User;
import com.github.code.zxs.auth.model.enums.AccountType;
import com.github.code.zxs.auth.model.enums.UserStateEnum;
import com.github.code.zxs.auth.model.enums.VerifyType;
import com.github.code.zxs.auth.model.vo.UserLoginDTO;
import com.github.code.zxs.auth.service.AuthService;
import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import com.github.code.zxs.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private AuthService authService;

    @Override
    public UserBO getUserBO(Long userId) {
        User user = userMapper.selectById(userId);
        return userConverter.entityToBO(user);
    }

    @Override
    public Date loginExpireAt(Long userId) {
        long expire = StpUtil.stpLogic.getTokenTimeoutByLoginId(userId);
        return DateUtils.plus(new Date(), expire, TimeUnit.SECONDS);
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        if (authService.existUsername(userRegisterDTO.getUsername()))
            throw new BaseException("该用户名已被注册");
        User user = userConverter.dtoToEntity(userRegisterDTO);
        user.setSalt(UUIDUtils.shortUUID());
        user.setPassword(encodePassword(user.getPassword(), user.getSalt()));
        userMapper.insert(user);
    }

    @Override
    public SaTokenInfo login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = findUserByUsername(username);
        if (user == null || user.getState() == UserStateEnum.DEACTIVATE
                || !encodePassword(password, user.getSalt()).equals(user.getPassword())) {
            throw new BaseException(ResponseStatusEnum.LOGIN_FAIL);
        }
        loginWithUserId(user.getId(), userLoginDTO.getRememberMe());
        SaSession session = StpUtil.getSession();
        session.set("username", user.getUsername());
        session.set("registerTime", user.getCreateTime());
        return StpUtil.getTokenInfo();
    }

    @Override
    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public void sendCaptcha(AccountType accountType, VerifyType verifyType, String account, Long expire, TimeUnit unit) {
        String cacheKey = RedisUtil.getSimpleCacheKey(accountType, verifyType, account);
        String value = CaptchaUtils.generateVerifyCode(6);
        if (expire == null)
            expire = 10L;
        if (unit == null)
            unit = TimeUnit.MINUTES;
        Optional.ofNullable(findUserByAccount(account, accountType))
                .orElseThrow(() -> new BaseException("该" + accountType.getDescription() + "未注册"));
        String title = "验证码";
        String content = String.format("【个人论坛】验证码 %s\n该验证码仅用于%s", value, verifyType.getDescription());
        redisUtil.set(cacheKey, value, expire, unit);
        switch (accountType) {
            case EMAIL:
                sendCaptchaToEmail(account, title, content);
                break;
        }
    }

    @Transactional
    @Override
    public void modifyPassword(AccountType accountType, VerifyType verifyType, String account, String password, String captcha) {
        String cacheKey = RedisUtil.getSimpleCacheKey(accountType, verifyType, account);
        String cacheCaptcha = (String) redisUtil.get(cacheKey);
        if (cacheCaptcha == null || !cacheCaptcha.equalsIgnoreCase(captcha))
            throw new BaseException("验证码错误");

        User user = Optional.ofNullable(findUserByAccount(account, accountType))
                .orElseThrow(() -> new BaseException("该" + accountType.getDescription() + "未注册"));
        String salt = UUIDUtils.shortUUID();

        int row = userMapper.update(User.builder().password(encodePassword(password, salt)).salt(salt).build(),
                new LambdaQueryWrapper<User>().eq(User::getEmail, account));
        if (row != 1) {
            log.warn("更新密码异常，{},{},{}", accountType, verifyType, account);
            throw new BaseException("修改失败");
        } else {
            //修改成功后，删除缓存中的验证码,登出该账号
            redisUtil.del(cacheKey);
            StpUtil.logout(user.getId());
        }
    }

    @Override
    public boolean existAccount(String account, AccountType accountType) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(getFieldName(account, accountType), account));
    }

    @Override
    public boolean existUsername(String username) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }


    private User findUserByAccount(String account, AccountType accountType) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(getFieldName(account, accountType), account));
    }

    private SFunction<User, String> getFieldName(String account, AccountType accountType) {
        switch (accountType) {
            case EMAIL:
                return User::getEmail;
            case PHONE:
                return User::getPhone;
            default:
                return null;
        }
    }

    private void sendCaptchaToEmail(String email, String title, String content) {
        EmailUtils.sendEmail(email, title, content, null);
    }


    private void loginWithUserId(Long userId, Boolean rememberMe) {
        StpUtil.login(userId, rememberMe);
        StpUtil.getTokenInfo();
    }

    private User findUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().setEntity(new User(username)));
    }

    private String encodePassword(String password, String salt) {
        return SaSecureUtil.md5BySalt(password, salt);
    }

}
