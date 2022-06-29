package com.github.code.zxs.storage.service.base;

import com.github.code.zxs.storage.model.dto.FileItemDTO;
import com.github.code.zxs.storage.model.dto.ImageItemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface BaseFileService {
    List<FileItemDTO> upload(List<MultipartFile> multipartFile);

    FileItemDTO download(String key);

    File download(String year, String month, String day, String filename);

    void delete(String key);

    void delete(String year, String month, String day, String filename);

    ImageItemDTO getThumbnail(String key);
}
