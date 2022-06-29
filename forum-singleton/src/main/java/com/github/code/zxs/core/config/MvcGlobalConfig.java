package com.github.code.zxs.core.config;


import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import com.github.code.zxs.auth.interceptor.UserContextInterceptor;
import com.github.code.zxs.core.interceptor.HttpContextInterceptor;
import com.github.code.zxs.core.support.converter.BaseEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcGlobalConfig implements WebMvcConfigurer {

    @Autowired
    private HttpContextInterceptor httpContextInterceptor;
    @Autowired
    private UserContextInterceptor userContextInterceptor;
    @Autowired
    private SaAnnotationInterceptor saAnnotationInterceptor;
    @Autowired
    private BaseEnumConverterFactory baseEnumConverterFactory;

    @Bean
    public SaAnnotationInterceptor getSaAnnotationInterceptor() {
        return new SaAnnotationInterceptor();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(baseEnumConverterFactory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpContextInterceptor).addPathPatterns("/**");
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**");
        registry.addInterceptor(saAnnotationInterceptor).addPathPatterns("/**");
    }
}
