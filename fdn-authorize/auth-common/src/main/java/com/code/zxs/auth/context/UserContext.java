package com.code.zxs.auth.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class UserContext {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Date> userCreateTimeHolder = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletResponse getResponse() {
        return responseHolder.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static Date getUserCreateTime() {
        return userCreateTimeHolder.get();
    }

    public static void setUserCreateTimeHolder(Date userCreateTime) {
        userCreateTimeHolder.set(userCreateTime);
    }

    public static void clear(){
        requestHolder.remove();
        responseHolder.remove();
        userIdHolder.remove();
        userCreateTimeHolder.remove();
    }
}
