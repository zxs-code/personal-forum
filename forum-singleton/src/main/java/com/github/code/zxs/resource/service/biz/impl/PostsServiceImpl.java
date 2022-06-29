package com.github.code.zxs.resource.service.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.resource.converter.PostsConverter;
import com.github.code.zxs.resource.mapper.PostsMapper;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.PostsDetailBO;
import com.github.code.zxs.resource.model.bo.PostsItemBO;
import com.github.code.zxs.resource.model.bo.UserViewDTO;
import com.github.code.zxs.resource.model.dto.*;
import com.github.code.zxs.resource.model.entity.Posts;
import com.github.code.zxs.resource.model.entity.PostsData;
import com.github.code.zxs.resource.model.entity.PostsState;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.biz.base.CollectService;
import com.github.code.zxs.resource.service.biz.base.LikeService;
import com.github.code.zxs.resource.service.biz.base.PostsService;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import com.github.code.zxs.resource.service.manager.PostsDataManager;
import com.github.code.zxs.resource.service.manager.PostsManager;
import com.github.code.zxs.resource.service.manager.PostsStateManager;
import com.github.code.zxs.search.service.manager.SearchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsServiceImpl self;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private PostsConverter postsConverter;
    @Autowired
    private PostsManager postsManager;
    @Autowired
    private PostsDataManager postsDataManager;
    @Autowired
    private PostsStateManager postsStateManager;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MessageProducer producer;
    @Autowired
    private SearchManager searchManager;
    @Autowired
    private PostsMapper postsMapper;

    public void publishPosts(PostsAddDTO postsAddDTO) {
        self.checkTags(postsAddDTO.getTags(), 10);
        Posts posts = postsConverter.dto2Entity(postsAddDTO);
        PostsData postsData = self.defaultPostsData(posts.getId());
        PostsState postsState = self.defaultPostsState(posts.getId(), postsAddDTO.getAnonymous());
        self.savePosts(posts, postsData, postsState);
    }

    @Transactional(rollbackFor = Exception.class)
    public void savePosts(Posts posts, PostsData postsData, PostsState postsState) {
        postsManager.save(posts);
        postsDataManager.save(postsData);
        postsStateManager.save(postsState);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePosts(PostsUpdateDTO postsUpdateDTO) {
        self.checkTags(postsUpdateDTO.getTags(), 10);

        long userId = UserContext.getId();
        long postsId = postsUpdateDTO.getId();

        Posts posts = postsConverter.dto2Entity(postsUpdateDTO);
        boolean update = postsManager.update(posts, postsManager.lambdaUpdate()
                .eq(Posts::getId, postsId)
                .eq(Posts::getCreateBy, userId)
        );
        if (!update)
            throw new UserEventException("帖子不存在或者不是发帖者", userId);
    }

    @Override
    public PostsDetailBO getPostsDetailById(long id) {
        Long userId = UserContext.getId();
        //增加阅读量
//        postsDataManager.incrPostsViews(id, userId, 1L);

        Posts postsBody = postsManager.get(id);
        PostsData postsData = postsDataManager.get(id);
        PostsState postsState = postsStateManager.get(id);

        PostsDetailBO postsDetailBO = postsConverter.toDetailBo(postsBody, postsData, postsState);
        postsDetailBO.setAuthor(postsState.getAnonymous()
                ? AuthorBO.ANONYMOUS_AUTHOR
                : Optional.ofNullable(userInfoService.getAuthorBoById(postsBody.getCreateBy()))
                .orElse(AuthorBO.DEACTIVATE_AUTHOR));

        ResourceDTO resourceDTO = new ResourceDTO(id, ResourceTypeEnum.POSTS);

        if (userId == null) {
            //未登录时，点赞，点踩，收藏状态为假
            postsDetailBO.setLikeState(LikeStateEnum.NEVER);
            postsDetailBO.setIsStars(false);
        } else {
            //登录后，设置点赞，点踩，收藏状态
            LikeStateEnum state = likeService.userLikeState(userId, resourceDTO);
            postsDetailBO.setLikeState(state);
            postsDetailBO.setIsStars(collectService.userIsCollect(userId, resourceDTO));
        }
        //添加评论
//        postsDetailBO.setComments(commentService.pageComment(resourceDTO, PageDTO.one10, CommentOrderEnum.HOT));
        //设置评论数
//        postsDetailBO.setComment(commentManager.countComment(resourceDTO));

        Date date = new Date();

        //登录状态下发送查看事件
        if (userId != null)
            producer.asyncSend(TopicConstant.USER_VIEW, userId.toString(), new UserViewDTO(userId, date, resourceDTO));
        return postsDetailBO;
    }

    @Override
    public void collectPosts(PostsCollectDTO postsCollectDTO) {
        CollectDTO collectDTO = new CollectDTO(UserContext.getId(), new ResourceDTO(postsCollectDTO.getId(), ResourceTypeEnum.POSTS), postsCollectDTO.getAddFavIds(), postsCollectDTO.getDelFavIds());
        collectService.collect(collectDTO);
    }

    @Override
    public String getTitle(Long id) {
        Posts posts = postsMapper.selectOne(new LambdaQueryWrapper<Posts>()
                .select(Posts::getTitle)
                .eq(Posts::getId, id)
        );
        return Optional.ofNullable(posts).map(Posts::getTitle).orElse(null);
    }

    @Override
    public PageResult<PostsItemBO> myPosts(PageDTO pageDTO) {
        Long userId = UserContext.getId();
        Page<Posts> postsPage = postsMapper.selectPage(Page.of(pageDTO.getCurPage(), pageDTO.getPageSize()),
                new LambdaQueryWrapper<Posts>()
                        .select(Posts::getId, Posts::getTitle)
                        .eq(Posts::getCreateBy, userId)
                        .orderByDesc(Posts::getUpdateTime));
        List<Posts> records = postsPage.getRecords();
        List<PostsItemBO> item = records.stream().map(e -> new PostsItemBO(e.getId(), e.getTitle())).collect(Collectors.toList());
        return PageResult.getPageResult(postsPage, item);
    }

    private PostsData defaultPostsData(Long postsId) {
        return PostsData.builder()
                .likes(0L)
                .dislikes(0L)
                .stars(0L)
                .views(0L)
                .visitor(0L)
                .comment(0L)
                .score(0.0)
                .build();
    }

    private PostsState defaultPostsState(Long postsId, Boolean anonymous) {
        return PostsState.builder()
                .id(postsId)
                .good(false)
                .top(false)
                .hide(false)
                .locked(false)
                .anonymous(anonymous).build();
    }

    private void checkTags(List<String> tags, int maxLength) {
        if (tags.stream().anyMatch(tag -> tag.length() > maxLength)) {
            throw new UserEventException("标签最大长度为10", UserContext.getId());
        }
    }
}
