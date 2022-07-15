package com.github.code.zxs.storage.exception;

import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class FileUploadException extends BaseException {
    private List<MultipartFile> multipartFiles;

    public FileUploadException(List<MultipartFile> multipartFiles) {
        this();
        this.multipartFiles = multipartFiles;
    }

    public FileUploadException() {
        super(ResponseStatusEnum.FILE_UPLOAD_FAIL.getCode(), ResponseStatusEnum.FILE_UPLOAD_FAIL.getMsg());
    }

    public FileUploadException(List<MultipartFile> multipartFiles, Throwable cause) {
        super(ResponseStatusEnum.FILE_UPLOAD_FAIL.getCode(), ResponseStatusEnum.FILE_UPLOAD_FAIL.getMsg(), cause);
        this.multipartFiles = multipartFiles;
    }
}
