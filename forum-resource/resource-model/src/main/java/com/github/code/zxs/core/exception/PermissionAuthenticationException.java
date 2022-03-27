package com.github.code.zxs.core.exception;


import com.github.code.zxs.core.enums.ResponseStatus;
import lombok.Data;

@Data
public class PermissionAuthenticationException extends BaseException{
    public PermissionAuthenticationException(int code, String message) {
        super(code, message);
    }

    public PermissionAuthenticationException(ResponseStatus status) {
        super(status);
    }
}
