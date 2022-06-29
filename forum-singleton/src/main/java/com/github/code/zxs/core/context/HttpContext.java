package com.github.code.zxs.core.context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class HttpContext {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> urlHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> uriHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> methodHolder = new ThreadLocal<>();
    /**
     * 客户端的真实IP地址
     */
    private static final ThreadLocal<String> clientIPHolder = new ThreadLocal<>();
    /**
     * IP链路，包含客户端和代理服务器的地址
     */
    private static final ThreadLocal<List<String>> ipLinkHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userAgentHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> refererHolder = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, String>> cookiesHolder = new ThreadLocal<>();

    public static HttpServletRequest getHttpServletRequest() {
        return requestHolder.get();
    }

    public static void setHttpServletRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletResponse getHttpServletResponse() {
        return responseHolder.get();
    }

    public static void setHttpServletResponse(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static String getURI() {
        return uriHolder.get();
    }

    public static void setURI(String uri) {
        uriHolder.set(uri);
    }

    public static String getURL() {
        return urlHolder.get();
    }

    public static void setURL(String url) {
        urlHolder.set(url);
    }

    public static String getMethod() {
        return methodHolder.get();
    }

    public static void setMethod(String method) {
        methodHolder.set(method);
    }


    public static String getClientIP() {
        return clientIPHolder.get();
    }

    public static void setClientIP(String clientIP) {
        clientIPHolder.set(clientIP);
    }

    public static List<String> getIPLink() {
        return ipLinkHolder.get();
    }

    public static void setIPLink(List<String> proxyIP) {
        ipLinkHolder.set(proxyIP);
    }

    public static String getUserAgent() {
        return userAgentHolder.get();
    }

    public static void setUserAgent(String userAgent) {
        userAgentHolder.set(userAgent);
    }

    public static String getReferer() {
        return refererHolder.get();
    }

    public static void setReferer(String referer) {
        refererHolder.set(referer);
    }

    public static Map<String, String> getCookies() {
        return cookiesHolder.get();
    }

    public static String getCookie(String name) {
        return cookiesHolder.get().get(name);
    }

    public static void setCookies(Map<String, String> cookies) {
        cookiesHolder.set(cookies);
    }

    public static void clear() {
        requestHolder.remove();
        responseHolder.remove();
        uriHolder.remove();
        urlHolder.remove();
        clientIPHolder.remove();
        ipLinkHolder.remove();
        userAgentHolder.remove();
        refererHolder.remove();
        cookiesHolder.remove();
        methodHolder.remove();
    }
}
