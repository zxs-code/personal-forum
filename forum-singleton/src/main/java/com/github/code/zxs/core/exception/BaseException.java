package com.github.code.zxs.core.exception;


import com.github.code.zxs.core.model.enums.ResponseStatusEnum;

public class BaseException extends RuntimeException {
    private final int code;

    public BaseException(String message) {
        this(ResponseStatusEnum.FAIL.getCode(), message);
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(ResponseStatusEnum status) {
        super(status.getMsg());
        this.code = status.getCode();
    }

    public BaseException(ResponseStatusEnum status, Throwable cause) {
        super(status.getMsg(), cause);
        this.code = status.getCode();
    }

    public int getCode() {
        return code;
    }
}
