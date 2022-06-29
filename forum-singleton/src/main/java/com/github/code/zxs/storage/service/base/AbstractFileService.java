package com.github.code.zxs.storage.service.base;

import com.github.code.zxs.storage.config.StorageConfig;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractFileService implements BaseFileService {
    @Autowired
    protected StorageConfig storageConfig;
}
