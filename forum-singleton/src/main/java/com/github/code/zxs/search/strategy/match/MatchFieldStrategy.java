package com.github.code.zxs.search.strategy.match;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;

public interface MatchFieldStrategy {
    /**
     * 获取对应的资源类型
     *
     * @return
     */
    ResourceTypeEnum getResourceType();

    /**
     * 获取需要匹配的字段
     *
     * @return
     */
    default String[] getMatchField(){
        return null;
    }
}
