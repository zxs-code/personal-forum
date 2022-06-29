package com.github.code.zxs.search.strategy.wrap;

import com.github.code.zxs.auth.model.enums.UserStateEnum;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.search.converter.PostsConverter;
import com.github.code.zxs.search.converter.UserConverter;
import com.github.code.zxs.search.model.bean.Searchable;
import com.github.code.zxs.search.model.bo.PostsItemBO;
import com.github.code.zxs.search.model.document.PostsDocument;
import com.github.code.zxs.search.model.document.UserDocument;
import com.github.code.zxs.search.service.manager.SearchManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostsItemWrapper implements ItemWrapper {
    private final UserConverter userConverter = UserConverter.defaultConverter();
    private final PostsConverter postsConverter = PostsConverter.defaultConverter();
    private final SearchManager searchManager;

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.POSTS;
    }

    public PostsItemWrapper(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Override
    public Object wrap(Searchable document) {
        PostsDocument posts = (PostsDocument) document;
        PostsItemBO postsItemBO = postsConverter.toItemBO(posts);
        postsItemBO.setAuthor(posts.getAnonymous()
                ? AuthorBO.ANONYMOUS_AUTHOR
                : Optional.ofNullable(searchManager.get(posts.getCreateBy().toString(), UserDocument.class))
                .filter(d -> d.getState() != UserStateEnum.DEACTIVATE)
                .map(userConverter::toAuthorBO)
                .orElse(AuthorBO.DEACTIVATE_AUTHOR));

        return postsItemBO;
    }
}
