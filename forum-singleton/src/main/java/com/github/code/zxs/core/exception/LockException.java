package com.github.code.zxs.core.exception;

import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Data;

@Data
public class LockException extends BaseException {
    private String key;

    public LockException(ResponseStatusEnum status, String key) {
        super(status);
        this.key = key;
    }

    public LockException(int code, String message, String key) {
        super(code, message);
        this.key = key;
    }

    public LockException(String message, String key) {
        super(ResponseStatusEnum.FAIL.getCode(), message);
        this.key = key;
    }

    public static long time = 0;

    /**
     * 不抓取栈信息，提供效率
     *
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
