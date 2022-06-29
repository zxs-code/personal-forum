package com.github.code.zxs.core.exception;

public class InvalidTokenException extends BaseException {
    private String token;

    public InvalidTokenException(String message, String token) {
        super(message);
        this.token = token;
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
