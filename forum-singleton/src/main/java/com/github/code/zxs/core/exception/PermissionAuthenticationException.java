package com.github.code.zxs.core.exception;


import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Data;

@Data
public class PermissionAuthenticationException extends BaseException{
    public PermissionAuthenticationException(int code, String message) {
        super(code, message);
    }

    public PermissionAuthenticationException(ResponseStatusEnum status) {
        super(status);
    }
}
