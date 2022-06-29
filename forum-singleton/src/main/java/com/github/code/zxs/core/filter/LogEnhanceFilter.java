package com.github.code.zxs.core.filter;


import com.github.code.zxs.core.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * 日志增强过滤器，其他模块引用时，需要使用{@link @ServletComponentScan}
 */
@Slf4j
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
        } catch (Exception e) {
            log.error("日志增强过滤器", e);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
