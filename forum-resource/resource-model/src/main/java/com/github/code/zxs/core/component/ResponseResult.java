package com.github.code.zxs.core.component;







@Data
@Builder
@AllArgsConstructor
public class ResponseResult<T> {
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

    public ResponseResult(ResponseStatus status) {
        this(status.getCode(), status.getMsg(), null);
    }

    public ResponseResult(ResponseStatus status, T data) {
        this(status.getCode(), status.getMsg(), data);
    }


    public <T> FeignResult<T> toFeignResult() {
        FeignResult<T> feignResult = new FeignResult<>();
        BeanUtils.copyProperties(this, feignResult);
        feignResult.setSuccess(this.code == ResponseStatus.SUCCESS.getCode());
        return feignResult;
    }
}
