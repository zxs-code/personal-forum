package com.github.code.zxs.auth.controller;

import com.github.code.zxs.auth.dto.TokenUserDTO;
import com.github.code.zxs.auth.enums.Device;
import com.github.code.zxs.auth.resolver.TokenResolver;
import com.github.code.zxs.auth.service.impl.JwtService;
import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.context.HttpContext;
import com.github.code.zxs.core.enums.ResponseStatus;
import com.github.code.zxs.core.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private TokenResolver resolver;

    @Autowired
    private JwtService jwtService;
//
//    @GetMapping("{test}")
//    @ExampleAnnotation
//    public Object test(@PathVariable String test, HttpServletRequest request) {
//        if(true)
//        throw new BaseException("haha");
//        System.out.println(jwtService.createRefreshToken(new TokenUserDTO()));
//        HttpServletResponse response = null;
//        return test;
//    }
    @GetMapping("test")
    public ResponseResult<String> test1(String test, HttpServletRequest request) {
        jwtService.createAccessToken(new TokenUserDTO());
        SpringContextUtils.getBean(Device.class);
        HttpContext.getHttpServletRequest().getRemoteHost();
        return new ResponseResult<>(ResponseStatus.NOT_FOUND);
    }
}
