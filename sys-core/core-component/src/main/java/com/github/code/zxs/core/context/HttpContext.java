package com.github.code.zxs.core.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HttpContext {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> urlHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> uriHolder = new ThreadLocal<>();
    /**
     * 获取客户端的真实IP地址
     */
    private static final ThreadLocal<String> clientIPHolder = new ThreadLocal<>();
    /**
     * 获取代理服务器的IP地址
     */
    private static final ThreadLocal<List<String>> proxyIPHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userAgentHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> refererHolder = new ThreadLocal<>();

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

    public static String getUri() {
        return uriHolder.get();
    }

    public static void setUri(String uri) {
        uriHolder.set(uri);
    }

    public static String getUrl() {
        return urlHolder.get();
    }

    public static void setUrl(String url) {
        urlHolder.set(url);
    }

    public static String getClientIP() {
        return clientIPHolder.get();
    }

    public static void setClientIP(String clientIP) {
        clientIPHolder.set(clientIP);
    }

    public static List<String> getProxyIP() {
        return proxyIPHolder.get();
    }

    public static void setProxyIP(List<String> proxyIP) {
        proxyIPHolder.set(proxyIP);
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

    public static void clear() {
        requestHolder.remove();
        responseHolder.remove();
        uriHolder.remove();
        urlHolder.remove();
        clientIPHolder.remove();
        proxyIPHolder.remove();
        userAgentHolder.remove();
        refererHolder.remove();
    }
}
