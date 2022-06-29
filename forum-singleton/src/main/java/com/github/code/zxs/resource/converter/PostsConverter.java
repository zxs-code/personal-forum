package com.github.code.zxs.resource.converter;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.resource.model.bo.PostsDetailBO;
import com.github.code.zxs.resource.model.dto.PostsAddDTO;
import com.github.code.zxs.resource.model.dto.PostsUpdateDTO;
import com.github.code.zxs.resource.model.entity.Posts;
import com.github.code.zxs.resource.model.entity.PostsData;
import com.github.code.zxs.resource.model.entity.PostsState;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import com.github.code.zxs.search.model.document.PostsDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostsConverter {
    public static final String POSTS_ID_KEY = "posts_id_key";

    @Autowired
    private DistributedIdGenerator idGenerator;

    public Posts dto2Entity(PostsAddDTO postsAddDTO) {
        List<String> tags = postsAddDTO.getTags();
        return Posts.builder()
                .id(idGenerator.getLongId(POSTS_ID_KEY))
                .title(postsAddDTO.getTitle())
                .content(postsAddDTO.getContent())
                .tags(JsonUtils.serialize(postsAddDTO.getTags())).build();
    }

    public Posts dto2Entity(PostsUpdateDTO postsUpdateDTO) {
        return Posts.builder()
                .id(postsUpdateDTO.getId())
                .title(postsUpdateDTO.getTitle())
                .content(postsUpdateDTO.getContent())
                .tags(JsonUtils.serialize(postsUpdateDTO.getTags())).build();
    }

    public PostsDetailBO document2DetailBo(PostsDocument postsDocument) {
        PostsDetailBO postsDetailBO = BeanUtils.copy(postsDocument, new PostsDetailBO());
        //设置标签
//        postsDetailBO.setTags(postsDocument.getTags());
        //是否是作者
        postsDetailBO.setIsAuthor(postsDocument.getCreateBy().equals(UserContext.getId()));
        postsDetailBO.setLikeState(LikeStateEnum.NEVER);
        postsDetailBO.setIsStars(false);
        return postsDetailBO;
    }

    public PostsDetailBO toDetailBo(Posts posts, PostsData data, PostsState state) {
        return PostsDetailBO.builder()
                .id(posts.getId())
                .title(posts.getTitle())
                .content(posts.getContent())
                .createTime(posts.getCreateTime())
                .updateTime(posts.getUpdateTime())
                .tags(JsonUtils.parseList(posts.getTags(), String.class))
                .views(data.getViews())
                .likes(data.getLikes())
                .dislikes(data.getDislikes())
                .comment(data.getComment())
                .stars(data.getStars())
                .top(state.getTop())
                .good(state.getGood())
                .hide(state.getHide())
                .locked(state.getLocked())
                .build();

    }
}
