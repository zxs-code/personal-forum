package com.github.code.zxs.search.strategy.filter;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.document.PostsDocument;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Component;

@Component
public class PostsSourceFilterStrategy implements SourceFilterStrategy {

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.POSTS;
    }

    @Override
    public SourceFilter getSourceFilter() {
        return new FetchSourceFilter(new String[0], new String[]{PostsDocument.Fields.content});
    }
}
