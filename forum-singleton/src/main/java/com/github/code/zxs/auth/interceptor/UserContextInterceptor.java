package com.github.code.zxs.auth.interceptor;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.github.code.zxs.auth.context.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Component
public class UserContextInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StpUtil.isLogin()) {
            SaSession session = StpUtil.getSession();
            UserContext.setId(Long.valueOf((String) StpUtil.getLoginId()));
            UserContext.setUsername(session.getString("username"));
            UserContext.setNickname(Optional.ofNullable(session.getString("nickname")).orElse(session.getString("username")));
            UserContext.setRegisterTime(session.getModel("registerTime", Date.class));
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
