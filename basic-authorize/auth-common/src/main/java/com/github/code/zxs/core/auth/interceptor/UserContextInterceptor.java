package com.github.code.zxs.core.auth.interceptor;











/**
 * 用户上下文拦截器。
 */
@Component
public class UserContextInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private TokenGlobalConfig tokenConfig;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = HttpContext.getCookie(tokenConfig.getAccessCookieName());
        if (accessToken != null) {
            UserContext.initUserInfo(accessToken);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

}
