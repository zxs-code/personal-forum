package com.github.code.zxs.core.auth.config;








@Configuration
public class AuthMvcConfig extends MvcGlobalConfig {
    private final UserContextInterceptor userContextInterceptor;

    private final PermissionInterceptor permissionInterceptor;

    @Autowired
    public AuthMvcConfig(UserContextInterceptor userContextInterceptor, PermissionInterceptor permissionInterceptor) {
        this.userContextInterceptor = userContextInterceptor;
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(userContextInterceptor).addPathPatterns("/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    }
}
