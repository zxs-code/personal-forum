package com.github.code.zxs.core.exception;


import com.github.code.zxs.core.model.enums.ResponseStatusEnum;

public class BaseException extends RuntimeException{
    private int code = ResponseStatusEnum.FAIL.getCode();
    public BaseException() {
    }

    public BaseException(int code,String message) {
        super(message);
        this.code = code;
    }
    public BaseException(ResponseStatusEnum status) {
        super(status.getMsg());
        this.code = status.getCode();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getCode() {
        return code;
    }
}
