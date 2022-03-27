package com.github.code.zxs.core.aspect;









/**
 * 出现异常先执行这个切面，再执行{@link ResponseWrapperAdvice}
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(Throwable.class)
    public ResponseResult<Object> handle(Throwable e) {
        log.error("执行异常", e);
        return new ResponseResult<>(ResponseStatus.FAIL);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseResult<Object> handle(BaseException e) {
        log.error("执行异常", e);
        return new ResponseResult<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResult<Object> handle(NoHandlerFoundException e) {
        log.error("资源未找到 {}", e.getRequestURL());
        return new ResponseResult<>(ResponseStatus.NOT_FOUND);
    }
}
