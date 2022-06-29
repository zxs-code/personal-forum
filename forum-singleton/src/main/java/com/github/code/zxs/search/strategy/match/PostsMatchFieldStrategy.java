package com.github.code.zxs.search.strategy.match;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.document.PostsDocument;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class PostsMatchFieldStrategy implements MatchFieldStrategy {

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.POSTS;
    }

    @Override
    public @NotNull String[] getMatchField() {
        return new String[]{
                PostsDocument.Fields.title,
                PostsDocument.Fields.content,
                PostsDocument.Fields.tags
        };
    }
}
