package com.github.code.zxs.storage.model.dto;

import com.github.code.zxs.storage.model.entity.FileItem;
import com.github.code.zxs.storage.model.enums.ContentTypeEnum;
import com.github.code.zxs.storage.model.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileItemDTO {
    private String key;
    private String name;
    private Long size;
    private String url;
    private String path;
    private Date timestamp;
    private Long uploaderId;
    private MultipartFile multipartFile;
    private File file;
    private FileTypeEnum fileType;
    private String hash;
    private ContentTypeEnum contentType;
    private Date deleteTime;

    public FileItem toFileItem(){
        FileItem fileItem = new FileItem();
        BeanUtils.copyProperties(this,fileItem);
        return fileItem;
    }
}
