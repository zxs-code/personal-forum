package com.github.code.zxs.auth.service.impl;

import com.github.code.zxs.auth.config.UUIDTokenConfig;
import com.github.code.zxs.auth.mapper.UserMapper;
import com.github.code.zxs.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnBean(UUIDTokenConfig.class)
public class AuthServiceImpl implements AuthService {

    private UserMapper userMapper;
}
