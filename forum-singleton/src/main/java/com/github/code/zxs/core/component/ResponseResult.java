package com.github.code.zxs.core.component;


import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {
    private String traceId;
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ResponseResult(ResponseStatusEnum status) {
        this(status.getCode(), status.getMsg(), null);
    }

    public ResponseResult(ResponseStatusEnum status, T data) {
        this(status.getCode(), status.getMsg(), data);
    }


    public <T> FeignResult<T> toFeignResult() {
        FeignResult<T> feignResult = new FeignResult<>();
        BeanUtils.copyProperties(this, feignResult);
        feignResult.setSuccess(this.code == ResponseStatusEnum.SUCCESS.getCode());
        return feignResult;
    }

    public static ResponseResult emptyResult() {
        return new ResponseResult(ResponseStatusEnum.FAIL);
    }
}
