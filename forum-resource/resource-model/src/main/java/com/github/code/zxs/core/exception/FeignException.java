package com.github.code.zxs.core.exception;

public class FeignException extends BaseException {
    public FeignException(int code, String message) {
        super(code, message);
    }

    public FeignException(String message) {
        super(message);
    }
}
