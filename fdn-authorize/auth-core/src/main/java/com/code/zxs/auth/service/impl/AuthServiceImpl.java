package com.code.zxs.auth.service.impl;

import com.code.zxs.auth.mapper.UserMapper;
import com.code.zxs.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
}
