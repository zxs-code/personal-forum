package com.github.code.zxs.resource.service.biz.base;

import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.resource.model.bo.PostsDetailBO;
import com.github.code.zxs.resource.model.bo.PostsItemBO;
import com.github.code.zxs.resource.model.dto.PostsAddDTO;
import com.github.code.zxs.resource.model.dto.PostsCollectDTO;
import com.github.code.zxs.resource.model.dto.PostsUpdateDTO;

public interface PostsService {

    void publishPosts(PostsAddDTO postsDTO);

    void updatePosts(PostsUpdateDTO postsDTO);

    PostsDetailBO getPostsDetailById(long id);

    void collectPosts(PostsCollectDTO postsCollectDTO);

    String getTitle(Long id);

    PageResult<PostsItemBO> myPosts(PageDTO pageDTO);
}
