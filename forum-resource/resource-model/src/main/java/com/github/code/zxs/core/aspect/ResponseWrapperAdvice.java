package com.github.code.zxs.core.aspect;














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
        try {
            //如果是feign请求，返回FeignResult
            if (HttpContext.getHttpServletRequest().getHeader(LogEnhanceFilter.FEIGN_TRACE_ID_HEADER) != null)
                return result.toFeignResult();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        result.setTraceId(MDC.get(LogEnhanceFilter.TRACE_ID));
        return result;
    }
}