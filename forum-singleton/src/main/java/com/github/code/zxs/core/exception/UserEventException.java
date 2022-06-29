package com.github.code.zxs.core.exception;

import lombok.Data;

@Data
public class UserEventException extends RuntimeException {
    private Long userId;

    public UserEventException(String message, Long userId) {
        super(message);
        this.userId = userId;
    }

    public UserEventException(String message, Throwable cause, Long userId) {
        super(message, cause);
        this.userId = userId;
    }
}
