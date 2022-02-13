package com.code.zxs.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 将配置类注入静态变量，在不被spring容器管理的类中引入配置
 */
@Component
public class ConfigStaticReference {

    @Autowired
    private JwtConfig jwtConfigTemp;
    private static JwtConfig jwtConfig;

    @PostConstruct
    public void init(){
        jwtConfig = this.jwtConfigTemp;
    }

    public static JwtConfig getJwtConfig() {
        return jwtConfig;
    }
}
