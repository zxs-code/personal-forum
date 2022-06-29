package com.github.code.zxs.storage.exception;

import com.github.code.zxs.core.exception.BaseException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileNotFoundException extends BaseException {
    private String key;
    private String path;
}
