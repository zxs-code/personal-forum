package com.github.code.zxs.core.component;

import com.github.code.zxs.core.enums.ResponseStatus;
import lombok.Data;

@Data
public class ResponseResult<T> {
    private String traceId;
    private int code;
    private String msg;
    private T data;

    public ResponseResult() {

    }

    public ResponseResult(int code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ResponseResult(ResponseStatus status, T data) {
        this(status.getCode(), status.getMsg(), data);
    }

    public ResponseResult(int code, String msg) {
        this(code, msg, null);
    }

    public ResponseResult(ResponseStatus status) {
        this(status.getCode(),status.getMsg(),null);
    }


    public ResponseResult with(int code) {
        this.code = code;
        return this;
    }
    public ResponseResult with(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponseResult with(ResponseStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        return this;
    }

    public ResponseResult with(T data) {
        this.data = data;
        return this;
    }
}
