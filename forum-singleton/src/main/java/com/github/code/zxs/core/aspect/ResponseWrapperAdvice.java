package com.github.code.zxs.core.aspect;


import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.filter.LogEnhanceFilter;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
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
        try {
            ResponseResult result = body instanceof ResponseResult
                    ? (ResponseResult) body
                    : new ResponseResult<>(ResponseStatusEnum.SUCCESS, body);
            //如果是feign请求，返回FeignResult
            if (request.getHeaders().getFirst(LogEnhanceFilter.FEIGN_TRACE_ID_HEADER) != null)
                return result.toFeignResult();

            result.setTraceId(MDC.get(LogEnhanceFilter.TRACE_ID));
            //Controller方法返回String时，会使用StringHttpMessageConverter,发生强转错误
            return result;
        } catch (Exception e) {
            log.error("包装返回结果出错：{}", body, e);
            ResponseResult result = new ResponseResult(ResponseStatusEnum.FAIL);
            result.setTraceId(MDC.get(LogEnhanceFilter.TRACE_ID));
            return result;
        }
    }
}