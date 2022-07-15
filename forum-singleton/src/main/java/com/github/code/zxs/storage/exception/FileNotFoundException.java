package com.github.code.zxs.storage.exception;

import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Getter;

@Getter
public class FileNotFoundException extends BaseException {
    private String path;

    public FileNotFoundException(String message, String path) {
        super(ResponseStatusEnum.FILE_NOT_FOUND.getCode(), message);
        this.path = path;
    }

    public FileNotFoundException(String path) {
        this(ResponseStatusEnum.FILE_NOT_FOUND.getMsg(), path);
    }
}
