package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.support.cache.HashCache;
import com.github.code.zxs.resource.mapper.PostsDataMapper;
import com.github.code.zxs.resource.model.entity.PostsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PostsDataManager extends ServiceImpl<PostsDataMapper, PostsData> {
    @Autowired
    private PostsDataManager self;

    private HashCache<Long, PostsData> cache;

    public PostsDataManager() {
        this.cache = new HashCache<>("postsData", PostsData.class);
        this.cache.setAfterWriteExpire(30);
        this.cache.setUnit(TimeUnit.DAYS);
    }

    public PostsData get(Long id) {
        return cache.computeIfAbsent(id, baseMapper::selectById);
    }

    public void put(Long id, String fieldName, Object value) {
        cache.asyncPut(id, PostsData.Fields.visitor, value);
    }

    public void incr(Long id, String fieldName, long delta) {
        cache.asyncIncr(id, fieldName, delta);
    }

    public void incr(Long id, Map<String, Double> deltaMap) {
        cache.asyncIncr(id, deltaMap);
    }

    public void delete(Long id) {
        baseMapper.deleteById(id);
        cache.delete(id);
    }

    @Scheduled(fixedRate = 60000)
    public void persist() {
        List<PostsData> all = cache.getAll();
        self.updateBatchById(all);
    }
}
