package com.github.code.zxs.auth.interceptor;

import com.github.code.zxs.core.context.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Cookie cookie = HttpUtils.getFirstCookie(request, ConfigStaticReference.getJwtConfig().getAccessCookieName());
//        String token = cookie.getValue();
        HttpContext.setHttpServletRequest(request);
        HttpContext.setHttpServletResponse(response);
        HttpContext.setHttpServletResponse(response);

        return super.preHandle(request, response, handler);
    }
}
