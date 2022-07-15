package com.github.code.zxs.message.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.message.model.bean.BaseResource;

public interface ResourceFactory {

    BaseResource getResource();

    ResourceTypeEnum getResourceType();

}
