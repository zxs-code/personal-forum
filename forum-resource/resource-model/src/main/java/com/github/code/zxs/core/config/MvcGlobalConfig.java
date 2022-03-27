package com.github.code.zxs.core.config;






public class MvcGlobalConfig implements WebMvcConfigurer {

    @Autowired
    private HttpContextInterceptor httpContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpContextInterceptor).addPathPatterns("/**");
    }
}
