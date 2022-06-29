package com.github.code.zxs.resource.service.manager;

import com.alicp.jetcache.anno.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.resource.mapper.PostsMapper;
import com.github.code.zxs.resource.model.entity.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsManager extends ServiceImpl<PostsMapper, Posts> {

    @Autowired
    private PostsManager self;

    @Cached(name = "posts", key = "#id", cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    @CacheRefresh(refresh = 20, stopRefreshAfterLastAccess = 60)
    public Posts get(Long id) {
        return baseMapper.selectById(id);
    }

    @CacheUpdate(name = "posts", key = "#posts.id", value = "#posts")
    public void savePosts(Posts posts) {
        self.save(posts);
    }

    @CacheInvalidate(name = "posts", key = "#id")
    public void deletePosts(Long id) {
        baseMapper.deleteById(id);
    }
}
