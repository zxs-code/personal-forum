package com.github.code.zxs.storage.service.Impl;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import com.github.code.zxs.core.util.FileUtils;
import com.github.code.zxs.core.util.StringUtils;
import com.github.code.zxs.core.util.UUIDUtils;
import com.github.code.zxs.storage.config.LocalStorageConfig;
import com.github.code.zxs.storage.exception.FileNotFoundException;
import com.github.code.zxs.storage.exception.FileUploadException;
import com.github.code.zxs.storage.mapper.FileMapper;
import com.github.code.zxs.storage.model.dto.FileItemDTO;
import com.github.code.zxs.storage.model.dto.ImageItemDTO;
import com.github.code.zxs.storage.model.entity.FileItem;
import com.github.code.zxs.storage.model.enums.ContentTypeEnum;
import com.github.code.zxs.storage.model.enums.FileTypeEnum;
import com.github.code.zxs.storage.service.base.AbstractFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@CacheConfig(cacheNames = "file-service")
public class LocalFileService extends AbstractFileService {

    @Autowired
    private LocalStorageConfig localStorageConfig;

    @Autowired
    private LocalFileService localFileService;

    @Autowired
    private FileMapper fileMapper;

    @Transactional
    public List<FileItemDTO> upload(List<MultipartFile> multipartFiles) {
        List<FileItemDTO> fileItemDTOSs = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                //构造DTO对象
                String key = UUIDUtils.randomUUID();
                String originName = multipartFile.getOriginalFilename();
                String suffix = FileUtils.getSuffix(originName);
                Date date = new Date();
                String path = localStorageConfig.getBasePathWithDate(date) + key + suffix;
                String url = storageConfig.getBaseUrlWithDate(date) + key + suffix;
                String hash = FileUtils.hash(multipartFile);

                FileItemDTO fileItemDTO = FileItemDTO.builder()
                        .key(key)
                        .name(originName)
                        .size(multipartFile.getSize())
                        .uploaderId(UserContext.getId())
                        .timestamp(date)
                        .path(path)
                        .url(url)
                        .contentType(FileUtils.getContentType(multipartFile))
                        .hash(hash)
                        .multipartFile(multipartFile).build();
                fileItemDTOSs.add(fileItemDTO);
                if (!canUpload(fileItemDTO))
                    throw new BaseException(ResponseStatusEnum.UNSUPPORTED_FILE_TYPE.getCode(),
                            fileItemDTO.getContentType().getSuffix() + "为不支持的文件类型");
            }
            //改为批量插入 TODO
            fileItemDTOSs.stream().map(FileItemDTO::toFileItem).forEach(fileItem -> {
                fileMapper.insert(fileItem);
            });
            //写入文件
            for (FileItemDTO fileItemDTO : fileItemDTOSs) {
                //重复文件创建硬链接
                String sourcePath = fileMapper.findPathByHash(fileItemDTO.getHash());
                if (FileUtils.exist(sourcePath))
                    FileUtils.createLink(fileItemDTO.getPath(), sourcePath);
                else
                    try (InputStream in = fileItemDTO.getMultipartFile().getInputStream()) {
                        FileUtils.writeFromStream(in, fileItemDTO.getPath());
                    }
            }
            log.info("{}-{}上传文件{}", UserContext.getId(), UserContext.getUsername(),
                    fileItemDTOSs.stream().map(FileItemDTO::getName).collect(Collectors.toList()));
            return fileItemDTOSs;
        } catch (Exception e) {
            log.error("{}-{}上传文件失败", UserContext.getId(), UserContext.getUsername(), e);
            try {
                for (FileItemDTO fileItemDTO : fileItemDTOSs) {
                    FileUtils.del(fileItemDTO.getPath());
                }
            } catch (Exception e1) {
                log.error("{}-{}文件回档失败", UserContext.getId(), UserContext.getUsername(), e1);
            }
            //不能直接抛出
            if (e instanceof BaseException)
                throw new BaseException(((BaseException) e).getCode(), e.getMessage());
            else
                throw new FileUploadException(ResponseStatusEnum.FILE_UPLOAD_FAIL);
        }
    }

    @Override
    @Cacheable(key = "'[' + #key + ']'")
    public FileItemDTO download(String key) {
        FileItemDTO fileItemDTO = Optional.ofNullable(fileMapper.selectById(key))
                .map(FileItem::toFileItemDTO)
                .orElseThrow(FileNotFoundException::new);
        File file = new File(fileItemDTO.getPath());
        if (!file.exists())
            throw new BaseException(ResponseStatusEnum.NOT_FOUND);
        fileItemDTO.setFile(file);
        fileItemDTO.setFileType(FileTypeEnum.FILE);
        return fileItemDTO;
    }

    @Override
    public File download(String year, String month, String day, String filename) {
        String path = StringUtils.join(LocalStorageConfig.SEPARATOR,
                localStorageConfig.getBasePath(),
                year, month, day, filename);
        File file = new File(path);
        if (!file.exists())
            throw new BaseException(ResponseStatusEnum.NOT_FOUND);
        return file;
    }

    @Override
    @Transactional
    public void delete(String key) {
        FileItemDTO fileItemDTO = localFileService.download(key);
        //检查是否有删除权限
        if (!canDelete(fileItemDTO))
            throw new BaseException(ResponseStatusEnum.FORBIDDEN);
        FileUtils.del(fileItemDTO.getFile());
        fileMapper.deleteById(fileItemDTO.getKey());
    }

    @Override
    public void delete(String year, String month, String day, String filename) {
        localFileService.delete(getKeyFromFilename(filename));
    }

    public ImageItemDTO getThumbnail(String key) {
        return null;
    }

    private boolean canDelete(FileItemDTO fileItemDTO) {
        return fileItemDTO.getUploaderId().equals(UserContext.getId());
    }

    private boolean canUpload(FileItemDTO fileItemDTO) {
        ContentTypeEnum[] allowContentType = storageConfig.getAllowContentType();
        for (ContentTypeEnum type : allowContentType)
            if (type == ContentTypeEnum.ALL || type == fileItemDTO.getContentType())
                return true;
        return false;
    }

    private String getKeyFromFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }

}
