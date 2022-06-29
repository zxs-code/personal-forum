package com.github.code.zxs.search.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.SpringContextUtils;
import com.github.code.zxs.search.strategy.filter.SourceFilterStrategy;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import java.util.HashMap;
import java.util.Map;

public class SourceFilterFactory {
    private static final Map<ResourceTypeEnum, SourceFilter> SOURCE_FILTER_MAP = new HashMap<>();

    static {
        BeanUtils.getSubClasses("", SourceFilterStrategy.class).stream()
                .map(SpringContextUtils::getBean)
                .forEach(filter -> SOURCE_FILTER_MAP.put(filter.getResourceType(), filter.getSourceFilter()));
    }

    public static SourceFilter getSourceFilter(ResourceTypeEnum type) {
        return SOURCE_FILTER_MAP.get(type);
    }
}
