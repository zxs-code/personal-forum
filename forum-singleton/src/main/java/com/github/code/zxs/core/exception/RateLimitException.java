package com.github.code.zxs.core.exception;

import com.github.code.zxs.core.model.dto.RedisCellResult;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Data;

@Data
public class RateLimitException extends BaseException{
    private String key;
    private RedisCellResult redisCellResult;

    public RateLimitException(ResponseStatusEnum status, String key, RedisCellResult redisCellResult) {
        super(status);
        this.key = key;
        this.redisCellResult = redisCellResult;
    }

    public RateLimitException(int code, String message, String key, RedisCellResult redisCellResult) {
        super(code, message);
        this.key = key;
        this.redisCellResult = redisCellResult;
    }
}
