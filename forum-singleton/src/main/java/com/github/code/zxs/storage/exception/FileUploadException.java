package com.github.code.zxs.storage.exception;

import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
public class FileUploadException extends BaseException {
    private MultipartFile multipartFile;
    private File file;

    public FileUploadException(ResponseStatusEnum status) {
        super(status);
    }

    public FileUploadException(ResponseStatusEnum status, MultipartFile multipartFile, File file) {
        super(status);
        this.multipartFile = multipartFile;
        this.file = file;
    }

    public FileUploadException(String message, MultipartFile multipartFile, File file) {
        super(message);
        this.multipartFile = multipartFile;
        this.file = file;
    }

    public FileUploadException(String message, MultipartFile multipartFile) {
        this(message, multipartFile, null);
    }

    public FileUploadException(String message, File file) {
        this(message, null, file);
    }
}
