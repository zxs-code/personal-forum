package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.support.cache.HashCache;
import com.github.code.zxs.resource.mapper.PostsDataMapper;
import com.github.code.zxs.resource.model.entity.PostsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class PostsDataManager extends ServiceImpl<PostsDataMapper, PostsData> {
    @Autowired
    private PostsDataManager self;

    private final HashCache<Long, PostsData> cache;

    public PostsDataManager() {
        this.cache = new HashCache<>("postsData", Long.class, PostsData.class);
        this.cache.setAfterWriteExpire(3);
        this.cache.setUnit(TimeUnit.DAYS);
    }

    public PostsData get(Long id) {
        PostsData data = cache.computeIfAbsent(id, baseMapper::selectById);
        if (!isValidCache(data)) {
            data = baseMapper.selectById(id);
            self.put(data);
        }
        return data;
    }

    public void put(Long id, String fieldName, Object value) {
        cache.asyncPut(id, fieldName, value);
    }

    public void put(PostsData value) {
        cache.asyncPutAll(Optional.ofNullable(value).map(PostsData::getId).orElseThrow(() -> new BaseException("id不能为空")), value);
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

    @Scheduled(fixedRate = 20000)
    public void persist() {
        Map<Long, PostsData> all = cache.getAll();
        Map<Boolean, List<Map.Entry<Long, PostsData>>> collect = all.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.partitioningBy(entry -> isValidCache(entry.getValue())));

        self.updateBatchById(collect.get(true).stream().map(Map.Entry::getValue).collect(Collectors.toList()));
        collect.get(false)
                .stream()
                .map(Map.Entry::getKey)
                .map(baseMapper::selectById)
                .forEach(self::put);
    }

    private boolean isValidCache(PostsData postsData) {
        return Optional.ofNullable(postsData).map(PostsData::getId).isPresent();
    }
}
