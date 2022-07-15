package com.github.code.zxs.core.aspect;


import cn.dev33.satoken.exception.NotLoginException;
import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import com.github.code.zxs.core.util.FileUtils;
import com.github.code.zxs.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常拦截器
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(Throwable.class)
    public ResponseResult<Object> handle(Throwable e) {
        log.error("执行异常", e);
        return new ResponseResult<>(ResponseStatusEnum.FAIL);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Object> handle(MethodArgumentNotValidException e) {
        log.error("接口参数异常", e);
        Object[] errors = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray();
        String errorMessage = StringUtils.join(errors);
        return new ResponseResult<>(ResponseStatusEnum.FAIL.getCode(), errorMessage);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseResult<Object> handle(BaseException e) {
        log.error("执行异常", e);
        return new ResponseResult<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseResult<Object> handle(NotLoginException e) {
        log.error("未登录异常", e);
        return new ResponseResult<>(ResponseStatusEnum.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResult<Object> handle(NoHandlerFoundException e) {
        log.warn("资源未找到 {}", e.getRequestURL());
        return new ResponseResult<>(ResponseStatusEnum.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult<Object> handle(MaxUploadSizeExceededException e) {
        ResponseStatusEnum status = ResponseStatusEnum.FILE_SIZE_LIMIT;
        Throwable rootCause = e.getRootCause();
        String message;
        if (rootCause instanceof SizeException) {
            String pretty = FileUtils.prettySize(((SizeException) rootCause).getPermittedSize());
            message = (rootCause instanceof FileSizeLimitExceededException ? "单个文件大小不能超过" : "总文件大小不能超过") + pretty;
        } else {
            message = status.getMsg();
        }
        log.warn(message, e);
        return new ResponseResult<>(status.getCode(), message);
    }

//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseResult<Object> handle(MaxUploadSizeExceededException e) {
//        ResponseStatusEnum status = ResponseStatusEnum.FILE_SIZE_LIMIT;
//        FileSizeLimitExceededException e1;
//        long permittedSize = e1.getPermittedSize();
//        String message = StringUtils.format(status.getMsg(), e.getMaxUploadSize());
//        log.warn(message);
//        return new ResponseResult<>(status.getCode(), message);
//    }
}
