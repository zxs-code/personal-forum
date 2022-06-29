package com.github.code.zxs.core.exception;


import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Data;

@Data
public class RoleAuthenticationException extends BaseException{
    public RoleAuthenticationException(int code, String message) {
        super(code, message);
    }

    public RoleAuthenticationException(ResponseStatusEnum status) {
        super(status);
    }
}
