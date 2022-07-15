package com.github.code.zxs.message.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.SpringContextUtils;

import java.util.HashMap;
import java.util.Map;

public class ResourceFactoryManager {

    private static final Map<ResourceTypeEnum, ResourceFactory> FACTORY_MAP = new HashMap<>();

    static {
        BeanUtils.getSubClasses("", ResourceFactory.class).stream()
                .map(SpringContextUtils::getBean)
                .forEach(wrapper -> FACTORY_MAP.put(wrapper.getResourceType(), wrapper));
    }

    public static ResourceFactory getFactory(ResourceTypeEnum resourceType) {
        return FACTORY_MAP.get(resourceType);
    }

}
