package com.github.code.zxs.core.util;





public class HttpUtils {
    public static Cookie getFirstCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals(cookieName))
                return cookie;
        return null;
    }

    public static Cookie[] getAllCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        Cookie[] result = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).toArray(Cookie[]::new);
        return result;
    }

    public static Cookie getCookie(String name, String value, String domain, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null)
            cookie.setDomain(domain);
        if (path != null)
            cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie getCookie(String name, String value) {
        return getCookie(name, value, null, null, -1);
    }

    public static Cookie getCookie(String name, String value, int maxAge) {
        return getCookie(name, value, null, null, maxAge);
    }
}
