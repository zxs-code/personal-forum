package com.github.code.zxs.storage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.code.zxs.core.context.HttpContext;
import com.github.code.zxs.core.model.enums.FileResponseTypeEnum;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.FileUtils;
import com.github.code.zxs.storage.model.dto.FileItemDTO;
import com.github.code.zxs.storage.model.vo.FileItemVO;
import com.github.code.zxs.storage.service.base.BaseFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Api("文件接口")
@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private final BaseFileService fileService;

    @Autowired
    public FileController(BaseFileService fileService) {
        this.fileService = fileService;
    }

    @SaCheckLogin
    @ApiOperation("上传文件")
    @PostMapping
    public FileItemVO upload(@RequestParam MultipartFile multipartFile) {
        List<FileItemDTO> list = fileService.upload(CollectionUtils.newArrayList(multipartFile));
        return toFileItemVO(0, list.get(0));
    }

    @SaCheckLogin
    @ApiOperation("上传多个文件")
    @PostMapping("multi")
    public List<FileItemVO> uploadMulti(
            @RequestParam(required = false) List<Integer> pos,
            @RequestParam List<MultipartFile> multipartFiles) {
        List<FileItemDTO> fileItemDTOs = fileService.upload(multipartFiles);
        List<FileItemVO> fileItemVOS = new ArrayList<>();
        for (int i = 0; i < fileItemDTOs.size(); i++)
            fileItemVOS.add(toFileItemVO(pos == null || pos.size() < i + 1 ? null : pos.get(i), fileItemDTOs.get(i)));

        return fileItemVOS;
//        AtomicInteger pos = new AtomicInteger();
//        return fileService.upload(multipartFiles).stream().map(fileItemDTO -> toFileItemVO(pos.getAndIncrement(), fileItemDTO)).collect(Collectors.toList());
    }

    @ApiOperation("获取文件")
    @GetMapping("{year}/{month}/{day}/{filename}")
    public void download(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String day,
            @PathVariable String filename,
            @RequestParam(required = false, defaultValue = "DEFAULT") FileResponseTypeEnum type) {
        File file = fileService.download(year, month, day, filename);
        //输出文件
        FileUtils.export(HttpContext.getHttpServletRequest(),
                HttpContext.getHttpServletResponse(),
                file, type);
    }

    @ApiOperation("获取文件")
    @GetMapping("{key}")
    public void download(
            @PathVariable String key,
            @RequestParam(required = false, defaultValue = "DEFAULT") FileResponseTypeEnum type) {
        FileItemDTO fileItemDTO = fileService.download(key);
        //输出文件
        FileUtils.export(HttpContext.getHttpServletRequest(),
                HttpContext.getHttpServletResponse(),
                fileItemDTO.getFile(), type);
    }


    @SaCheckLogin
    @ApiOperation("删除文件")
    @DeleteMapping("{year}/{month}/{day}/{filename}")
    public void deleteFile(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String day,
            @PathVariable String filename) {
        fileService.delete(year, month, day, filename);
    }

    @SaCheckLogin
    @ApiOperation("删除文件")
    @DeleteMapping("{key}")
    public void deleteFile(
            @PathVariable String key) {
        fileService.delete(key);
    }


    @ApiOperation("获取缩略图")
    @GetMapping("{key}/thumbnail")
    public void getThumbnail(@PathVariable String key) {

    }


    private FileItemVO toFileItemVO(Integer pos, FileItemDTO fileItemDTO) {
        return new FileItemVO(pos, fileItemDTO.getUrl());
    }
}
