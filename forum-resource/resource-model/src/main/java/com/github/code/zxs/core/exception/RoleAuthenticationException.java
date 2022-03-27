package com.github.code.zxs.core.exception;




@Data
public class RoleAuthenticationException extends BaseException{
    public RoleAuthenticationException(int code, String message) {
        super(code, message);
    }

    public RoleAuthenticationException(ResponseStatus status) {
        super(status);
    }
}
