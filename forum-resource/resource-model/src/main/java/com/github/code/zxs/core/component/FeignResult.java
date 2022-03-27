package com.github.code.zxs.core.component;








@Data
@AllArgsConstructor
@Builder
public class FeignResult<T> {
    private Integer code;
    private String msg;
    private T data;
    private Boolean success;

    public FeignResult() {
    }

    public FeignResult(ResponseStatus status, T data, Boolean success) {
        this(status.getCode(), status.getMsg(), data, success);
    }

    public FeignResult(Integer code, String msg, Boolean success) {
        this(code, msg, null, success);
    }

    public FeignResult(ResponseStatus status, Boolean success) {
        this(status.getCode(), status.getMsg(), null, success);
    }


    public T assertSuccess() {
        if (!Boolean.TRUE.equals(success))
            throw new FeignException(code, msg);
        return getData();
    }
}
