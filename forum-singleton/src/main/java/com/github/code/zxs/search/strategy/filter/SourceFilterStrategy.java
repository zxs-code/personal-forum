package com.github.code.zxs.search.strategy.filter;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import javax.annotation.Nullable;

public interface SourceFilterStrategy {
    /**
     * 获取过滤器对应的资源类型
     *
     * @return
     */
    ResourceTypeEnum getResourceType();

    /**
     * 获取返回字段过滤器
     *
     * @return
     */
    @Nullable
    default SourceFilter getSourceFilter() {
        return null;
    }
}
