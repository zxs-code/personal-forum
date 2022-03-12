package com.github.code.zxs.core.aspect;

import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.enums.ResponseStatus;
import com.github.code.zxs.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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
