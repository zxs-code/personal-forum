package com.github.code.zxs.core.aspect;

import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.enums.ResponseStatus;
import com.github.code.zxs.core.filter.LogEnhanceFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class ResponseWrapperAdvice implements ResponseBodyAdvice {
    /**
     * 判断是否要执行beforeBodyWrite方法，true为执行，false不执行
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 处理controller的返回值
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        ResponseResult result = body instanceof ResponseResult
                ? (ResponseResult) body
                : new ResponseResult<>(ResponseStatus.SUCCESS, body);

        result.setTraceId(MDC.get(LogEnhanceFilter.TRACE_ID));
        return result;
    }
}