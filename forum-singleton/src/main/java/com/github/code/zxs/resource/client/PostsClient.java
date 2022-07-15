package com.github.code.zxs.resource.client;

import com.github.code.zxs.resource.model.bo.PostsDetailBO;
import com.github.code.zxs.resource.service.biz.base.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 改为远端调用 TODO
 */
@Component
public class PostsClient {
    @Autowired
    private PostsService postsService;

    public PostsDetailBO getPostsDetail(@PathVariable Long id) {
        return postsService.getPostsDetailById(id);
    }
}
