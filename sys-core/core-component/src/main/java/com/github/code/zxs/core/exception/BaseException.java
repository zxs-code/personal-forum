package com.github.code.zxs.core.exception;

import com.github.code.zxs.core.enums.ResponseStatus;

public class BaseException extends RuntimeException{
    private int code;
    public BaseException() {
    }

    public BaseException(int code,String message) {
        super(message);
        this.code = code;
    }
    public BaseException(ResponseStatus status) {
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
