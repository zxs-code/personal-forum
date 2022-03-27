package com.github.code.zxs.core.filter;










/**
 * 日志增强过滤器，其他模块引用时，需要使用{@link @ServletComponentScan}
 */
@Component
@WebFilter(filterName = "logEnhanceFilter", urlPatterns = "/*")
public class LogEnhanceFilter implements Filter {
    public static final String TRACE_ID = "traceId";
    public static final String FEIGN_TRACE_ID_HEADER = "Feign-Trace-Id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            //如果请求头中没有traceId，则创建一个
            String traceId = Optional.ofNullable(httpServletRequest.getHeader(FEIGN_TRACE_ID_HEADER)).orElse(UUIDUtils.randomCompactUUID());
            //放入链路跟踪id
            MDC.put(TRACE_ID, traceId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
