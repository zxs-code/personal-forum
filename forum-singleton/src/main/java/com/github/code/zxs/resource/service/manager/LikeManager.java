package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.model.enums.LockWaitStrategyEnum;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.resource.mapper.LikeMapper;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import com.github.code.zxs.resource.support.result.LikeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class LikeManager extends ServiceImpl<LikeMapper, Like> {
    private static final String LIKE_ID_KEY = "like_id_key";
    @Autowired
    private DistributedIdGenerator idGenerator;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LikeManager self;

    @Lock(prefix = "batchInsertLike", waitStrategy = LockWaitStrategyEnum.BLOCK)
    @Transactional(rollbackFor = Exception.class)
    public Long insertIgnore(List<Like> likes) {
        if (CollectionUtils.isEmpty(likes))
            return 0L;
        return baseMapper.insertIgnore(likes);
    }

    //使用分布式锁 解决 INSERT ON DUPLICATE KEY 死锁问题
    @Lock(prefix = "batchInsertLike", waitStrategy = LockWaitStrategyEnum.BLOCK)
    @Transactional(rollbackFor = Exception.class)
    public Long saveInDatabase(List<Like> likes) {
        if (CollectionUtils.isEmpty(likes))
            return 0L;
        return baseMapper.saveOrUpdate(likes);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<Like> likes) {
        if (CollectionUtils.isEmpty(likes))
            return;
        baseMapper.updateBatch(likes);
    }

    public Long count(ResourceDTO resourceDTO, LikeStateEnum state) {
        return Optional.ofNullable(self.count(CollectionUtils.newArrayList(resourceDTO), state))
                .map(list -> list.get(0)).orElse(0L);
    }

    public List<Long> count(List<ResourceDTO> resourceDTOs, LikeStateEnum state) {
        if (state != LikeStateEnum.LIKE && state != LikeStateEnum.DISLIKE)
            return Collections.emptyList();

        List<String> keyList = resourceDTOs.stream().map(e -> getResourceCountKey(e, state)).collect(Collectors.toList());
        return CollectionUtils.intToLongDefaultZero(redisUtil.multiGet(keyList));
    }

    public List<LikeStateEnum> userLikeState(Long userId, ResourceTypeEnum resourceType, List<Long> resourceIds) {
        checkCache(userId, resourceType);

        String userKey = getUserKey(userId, resourceType);
        List<String> keys = resourceIds.stream().map(Object::toString).collect(Collectors.toList());
        return redisUtil.hMultiGet(userKey, keys)
                .stream()
                .map(Integer.class::cast)
                .map(state -> Optional.ofNullable(state).map(LikeStateEnum::valueOf).orElse(LikeStateEnum.NEVER))
                .collect(Collectors.toList());
    }

    public LikeStateEnum userLikeState(Long userId, ResourceDTO resourceDTO) {
        checkCache(userId, resourceDTO.getType());

        String userKey = getUserKey(userId, resourceDTO.getType());
        return Optional.ofNullable(redisUtil.hGet(userKey, resourceDTO.getId().toString()))
                .map(Integer.class::cast)
                .map(LikeStateEnum::valueOf)
                .orElse(LikeStateEnum.NEVER);
    }

    /**
     * 保存喜欢信息，返回执行结果和旧状态
     *
     * @param likes
     * @return
     */
    public List<LikeResult> saveInCache(List<Like> likes) {
        List<LikeResult> results = new ArrayList<>();
        for (Like like : likes) {
            ResourceTypeEnum resourceType = like.getResourceType();
            long resourceId = like.getResourceId();
            long userId = like.getCreateBy();
            LikeStateEnum newState = like.getState();

            checkCache(userId, resourceType);
            //用户资源 key
            String userKey = getUserKey(userId, resourceType);
            List<String> keys = CollectionUtils.asList(userKey);
            DefaultRedisScript<Integer> script = ScriptManager.getScript("Like");
            //写入新状态，返回旧状态
            int stateCode = Optional.ofNullable(redisUtil.execute(script, keys, resourceId, newState.getCode())).orElse(4);

            if (stateCode == -1)
                results.add(new LikeResult(false, null, null, null));

            LikeStateEnum oldState = LikeStateEnum.valueOf(stateCode);
            results.add(new LikeResult(true, oldState, newState, like));
        }
        return results;
    }


    public PageResult<Long> getRecentLikeResourceId(Long userId, ResourceTypeEnum resourceType, PageDTO pageDTO) {
        checkCache(userId, resourceType);
        String recentLikeKey = getRecentLikeKey(userId, resourceType);
        long start = pageDTO.getStart();
        long end = pageDTO.getEnd();

        Long count = redisUtil.zSize(recentLikeKey);
        List<Long> item = CollectionUtils.castToList(
                redisUtil.zReverseRange(recentLikeKey, start, end),
                Long.class);
        return new PageResult<>(pageDTO, count, item);
    }


    private String getUserKey(long userId, ResourceTypeEnum resourceType) {
        return RedisUtil.getSimpleCacheKey("likeState", userId, resourceType);
    }

    private String getRecentLikeKey(long userId, ResourceTypeEnum resourceType) {
        return RedisUtil.getSimpleCacheKey("recentLike", userId, resourceType);
    }

    private String getResourceCountKey(ResourceDTO resourceDTO, LikeStateEnum state) {
        return RedisUtil.getSimpleCacheKey(state, resourceDTO.getType(), resourceDTO.getId(), "count");
    }

    public List<Like> listLike(long userId, ResourceTypeEnum resourceType) {
        return baseMapper.selectList(new LambdaQueryWrapper<Like>()
                .eq(Like::getCreateBy, userId)
                .eq(Like::getResourceType, resourceType));
    }

    protected boolean checkCache(Long userId, ResourceTypeEnum resourceType) {
        if (self.hasCache(userId, resourceType)) {
            return true;
        }
        return self.updateCache(userId, resourceType, 2, TimeUnit.HOURS);
    }

    protected boolean hasCache(Long userId, ResourceTypeEnum resourceType) {
        String userKey = getUserKey(userId, resourceType);
        return redisUtil.hasKey(userKey);
    }

    @Lock(prefix = "updateLikeCache", key = "#userId")
    protected boolean updateCache(Long userId, ResourceTypeEnum resourceType, long timeout, TimeUnit unit) {
        if (self.hasCache(userId, resourceType))
            return true;

        String userKey = getUserKey(userId, resourceType);
        String recentLikeKey = getRecentLikeKey(userId, resourceType);

        List<Like> likes = self.listLike(userId, resourceType);
        //如果数据库查询数据为空，在缓存中添加空数据
        if (CollectionUtils.isEmpty(likes))
            likes = CollectionUtils.asList(new Like(resourceType, -1L, LikeStateEnum.NEVER));

        Map<String, Object> map = likes.stream().collect(Collectors.toMap(e -> e.getResourceId().toString(), e -> e.getState().getCode()));
        Set set = likes.stream()
                .filter(e -> e.getState() == LikeStateEnum.LIKE)
                .map(e -> new DefaultTypedTuple(e.getResourceId(), (double) e.getUpdateTime().getTime()))
                .collect(Collectors.toSet());

        redisUtil.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                ops.opsForHash().putAll(userKey, map);
                ops.expire(userKey, timeout, unit);
                if (CollectionUtils.isNotEmpty(set)) {
                    ops.opsForZSet().add(recentLikeKey, set);
                    ops.expire(recentLikeKey, timeout, unit);
                }
                return null;
            }
        });
        return true;
    }

    public long generateId(){
        return idGenerator.getLongId(LIKE_ID_KEY);
    }
}
