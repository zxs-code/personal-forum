package com.github.code.zxs.core.interceptor;












/**
 * Http上下文拦截器，需要在其他拦截器之前。
 */
@Component
public class HttpContextInterceptor extends HandlerInterceptorAdapter {
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String SEPARATOR = ",";

    public HttpContextInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpContext.setHttpServletRequest(request);
        HttpContext.setHttpServletResponse(response);
        HttpContext.setURI(request.getRequestURI());
        HttpContext.setURL(request.getRequestURL().toString());
        HttpContext.setMethod(request.getMethod());
        HttpContext.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        HttpContext.setReferer(request.getHeader(HttpHeaders.REFERER));
        HttpContext.setReferer(request.getHeader(HttpHeaders.REFERER));
        HttpContext.setCookies(getCookieMap(request));
        //获取ip链路
        List<String> ipLink = getIPLink(request);
        HttpContext.setClientIP(ipLink.get(0));
        HttpContext.setIPLink(ipLink);

        return true;
    }

    private Map<String, String> getCookieMap(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            return Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private List<String> getIPLink(HttpServletRequest request) {
        try {
            List<String> ipLink = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                    .map(header -> header.split(SEPARATOR))
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
            ipLink.add(request.getRemoteAddr());
            return ipLink;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HttpContext.clear();
    }
}
