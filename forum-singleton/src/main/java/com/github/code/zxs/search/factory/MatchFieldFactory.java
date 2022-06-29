package com.github.code.zxs.search.factory;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.SpringContextUtils;
import com.github.code.zxs.search.strategy.match.MatchFieldStrategy;

import java.util.HashMap;
import java.util.Map;

public class MatchFieldFactory {
    private static final Map<ResourceTypeEnum, String[]> MATCH_FIELD_STRATEGY_MAP = new HashMap<>();

    static {
        BeanUtils.getSubClasses("", MatchFieldStrategy.class).stream()
                .map(SpringContextUtils::getBean)
                .forEach(filter -> MATCH_FIELD_STRATEGY_MAP.put(filter.getResourceType(), filter.getMatchField()));
    }

    public static String[] getMatchField(ResourceTypeEnum type) {
        return MATCH_FIELD_STRATEGY_MAP.get(type);
    }
}
