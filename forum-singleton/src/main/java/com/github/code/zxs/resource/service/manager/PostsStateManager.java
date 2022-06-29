package com.github.code.zxs.resource.service.manager;

import com.alicp.jetcache.anno.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.resource.mapper.PostsStateMapper;
import com.github.code.zxs.resource.model.entity.PostsState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsStateManager extends ServiceImpl<PostsStateMapper, PostsState> {
    @Autowired
    private PostsStateManager self;

    @Cached(name = "postsState", key = "#id", cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    @CacheRefresh(refresh = 20, stopRefreshAfterLastAccess = 60)
    public PostsState get(Long id) {
        return baseMapper.selectById(id);
    }

    @CacheUpdate(name = "postsState", key = "#postsState.id", value = "#postsState")
    public void savePostsState(PostsState postsState) {
        self.save(postsState);
    }

    @CacheInvalidate(name = "postsState", key = "#id")
    public void deletePostsState(Long id) {
        baseMapper.deleteById(id);
    }
}
