package com.github.code.zxs.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class HttpUtils {
    public static Cookie getFirstCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies)
            if(cookie.getName().equals(cookieName))
                return cookie;
            return null;
    }

    public static Cookie[] getAllCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        Cookie[] result = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).toArray(Cookie[]::new);
        return result;
    }
}
