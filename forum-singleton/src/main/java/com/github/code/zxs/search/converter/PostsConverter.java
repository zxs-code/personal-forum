package com.github.code.zxs.search.converter;


import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.search.model.bo.PostsItemBO;
import com.github.code.zxs.search.model.document.PostsDocument;

import java.util.Map;

public class PostsConverter {

    private static final PostsConverter DEFAULT_CONVERTER = new PostsConverter();

    public static PostsConverter defaultConverter() {
        return DEFAULT_CONVERTER;
    }

    public PostsItemBO toItemBO(PostsDocument document, Map<Long, AuthorBO> authorMap) {
        return PostsItemBO.builder()
                .id(document.getId())
                .title(document.getTitle())
                .summary(document.getSummary())
                .views(document.getViews())
                .comment(document.getComment())
                .likes(document.getLikes())
                .dislikes(document.getDislikes())
                .top(document.getTop())
                .good(document.getGood())
                .locked(document.getLocked())
                .hide(document.getHide())
                .previewImg(document.getPreviewImg())
                .srcImg(document.getSrcImg())
                .createTime(document.getCreateTime())
                .updateTime(document.getUpdateTime())
                .author(document.getAnonymous()
                        ? AuthorBO.ANONYMOUS_AUTHOR
                        : authorMap.getOrDefault(document.getCreateBy(), AuthorBO.DEACTIVATE_AUTHOR))
                .build();
    }

    public PostsItemBO toItemBO(PostsDocument document) {
        return PostsItemBO.builder()
                .id(document.getId())
                .title(document.getTitle())
                .summary(document.getSummary())
                .views(document.getViews())
                .comment(document.getComment())
                .likes(document.getLikes())
                .dislikes(document.getDislikes())
                .top(document.getTop())
                .good(document.getGood())
                .locked(document.getLocked())
                .hide(document.getHide())
                .previewImg(document.getPreviewImg())
                .srcImg(document.getSrcImg())
                .createTime(document.getCreateTime())
                .updateTime(document.getUpdateTime())
                .build();
    }
}
