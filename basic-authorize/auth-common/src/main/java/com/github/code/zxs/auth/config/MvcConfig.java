package com.github.code.zxs.auth.config;

import com.github.code.zxs.auth.interceptor.AuthInterceptor;
import com.github.code.zxs.core.handler.ExampleHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ExampleHandler()).addPathPatterns("/**");
    }
}
