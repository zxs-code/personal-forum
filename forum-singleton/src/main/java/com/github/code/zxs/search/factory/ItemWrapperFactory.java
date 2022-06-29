package com.github.code.zxs.search.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.SpringContextUtils;
import com.github.code.zxs.search.strategy.wrap.ItemWrapper;

import java.util.HashMap;
import java.util.Map;

public class ItemWrapperFactory {
    private static final Map<ResourceTypeEnum, ItemWrapper> WRAPPER_MAP = new HashMap<>();

    static {
        BeanUtils.getSubClasses("", ItemWrapper.class).stream()
                .map(SpringContextUtils::getBean)
                .forEach(wrapper -> WRAPPER_MAP.put(wrapper.getResourceType(), wrapper));
    }

    public static ItemWrapper getItemWrapper(ResourceTypeEnum type) {
        return WRAPPER_MAP.get(type);
    }
}
