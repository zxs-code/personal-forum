package com.github.code.zxs.resource.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.code.zxs.core.annotation.RateLimit;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.component.ResponseResult;
import com.github.code.zxs.core.generator.UserIdKeyGenerator;
import com.github.code.zxs.core.model.enums.RateLimitStrategyEnum;
import com.github.code.zxs.resource.model.bo.PostsDetailBO;
import com.github.code.zxs.resource.model.bo.PostsItemBO;
import com.github.code.zxs.resource.model.dto.CommonLikeDTO;
import com.github.code.zxs.resource.model.dto.PostsAddDTO;
import com.github.code.zxs.resource.model.dto.PostsCollectDTO;
import com.github.code.zxs.resource.model.dto.PostsUpdateDTO;
import com.github.code.zxs.resource.service.biz.base.PostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api("帖子接口")
@RestController
@Validated
@RequestMapping("posts")
public class PostsController {
    @Autowired
    private PostsService postsService;

    @SaCheckLogin
    @ApiOperation("发布帖子")
    @PostMapping
    @RateLimit(prefix = "posts::issue::", keyGenerator = UserIdKeyGenerator.class,
            countPerPeriod = 1, message = "60秒内只能发布一次帖子",
            strategy = RateLimitStrategyEnum.AFTER_RETURNING)
    public void issuePosts(@RequestBody @Valid PostsAddDTO postsAddDTO) {
        if (postsAddDTO.getAnonymous() == null)
            postsAddDTO.setAnonymous(false);

        postsService.publishPosts(postsAddDTO);
    }

    @ApiOperation("更新帖子")
    @PutMapping
    public void updatePosts(@RequestBody PostsUpdateDTO postsUpdateDTO) {
        postsService.updatePosts(postsUpdateDTO);
    }

    @ApiOperation("获取某个帖子详情")
    @ApiImplicitParam(name = "id", value = "帖子id")
    @GetMapping("{id}/detail")
    public PostsDetailBO getPostsDetail(@PathVariable Long id) {
        return postsService.getPostsDetailById(id);
    }

    @SaCheckLogin
    @ApiOperation("点赞点踩帖子")
    @PutMapping("like")
    public ResponseResult like(@RequestBody CommonLikeDTO commonLikeDTO) {
//        LikeResult likeResult = postsService.likePosts(commonLikeDTO);
//        if (likeResult == null)
//            return new ResponseResult(-1, "请勿频繁点赞");
        return null;
    }

    @SaCheckLogin
    @ApiOperation("收藏帖子")
    @PutMapping("collect")
    public void collect(@RequestBody PostsCollectDTO postsCollectDTO) {
        postsService.collectPosts(postsCollectDTO);
    }


    @GetMapping("owner")
    public PageResult<PostsItemBO> PostItemBO(@ModelAttribute PageDTO pageDTO) {
        return postsService.myPosts(pageDTO);
    }
}
