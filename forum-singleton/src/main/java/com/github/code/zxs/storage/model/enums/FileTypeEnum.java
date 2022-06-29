package com.github.code.zxs.storage.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    FILE("file", "文件"),
    DIRECTORY("directory", "文件夹");

    private final String key;
    private final String description;
}
