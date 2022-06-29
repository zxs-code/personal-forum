package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.model.enums.LockFailStrategyEnum;
import com.github.code.zxs.core.model.enums.LockWaitStrategyEnum;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.resource.mapper.CommentMapper;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Comment;
import com.github.code.zxs.resource.model.enums.CommentOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommentManager extends ServiceImpl<CommentMapper, Comment> {
    public static final long COMMENT_EXPIRE = 7;
    public static final TimeUnit COMMENT_EXPIRE_UNIT = TimeUnit.DAYS;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommentManager self;

    public void persistComment(List<Comment> comments) {
        self.saveBatch(comments);
    }

    public void cacheComment(List<Comment> comments) {
        for (Comment comment : comments) {
            ResourceDTO dto = new ResourceDTO(comment.getResourceId(), comment.getResourceType());
            String commentKey = getCommentKey(comment.getId());
            redisUtil.executePipelined(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.opsForHash().putAll(commentKey, JsonUtils.toMap(comment));
                    operations.expire(commentKey, COMMENT_EXPIRE, COMMENT_EXPIRE_UNIT);
                    operations.opsForZSet().add(getCommentKey(dto, CommentOrderEnum.TIME), comment.getId(), comment.getSeq());
                    operations.opsForZSet().add(getCommentKey(dto, CommentOrderEnum.HOT), comment.getId(), comment.getLikes());
                    return null;
                }
            });
//            redisUtil.hPutAll(commentKey, JsonUtils.toMap(comment));
//            redisUtil.expire(commentKey, COMMENT_EXPIRE, COMMENT_EXPIRE_UNIT);
//            redisUtil.zAdd(getCommentKey(dto, CommentOrderEnum.TIME), comment.getId(), comment.getSeq());
//            redisUtil.zAdd(getCommentKey(dto, CommentOrderEnum.HOT), comment.getId(), comment.getLikes());
        }
    }

    public void updateLikeOrDislikeCount(List<Comment> comments) {
        //更新热评中的点赞数
        for (Comment comment : comments) {
            Long commentId = comment.getId();
            Long likeCount = comment.getLikes();
            Long dislikeCount = comment.getLikes();
            if (likeCount != null) {
                String commentKey = getCommentKey(commentId);
                ResourceTypeEnum resourceType = (ResourceTypeEnum) redisUtil.hGet(commentKey, Comment.Fields.resourceType);
                long resourceId = (long) redisUtil.hGet(commentKey, Comment.Fields.resourceId);
                ResourceDTO commentBody = new ResourceDTO(resourceId, resourceType);
                redisUtil.zAdd(getCommentKey(commentBody, CommentOrderEnum.HOT), commentId, likeCount);

                //更新评论实体的点赞数
                updateCacheField(commentId, Comment.Fields.likes, likeCount);
            }
            if (dislikeCount != null) {
                //更新评论实体的点踩数
                updateCacheField(commentId, Comment.Fields.dislikes, dislikeCount);
            }
        }
        //更新数据库
        self.updateBatchById(comments);
    }


    public Comment getCommentById(Long commentId) {
        return listComment(CollectionUtils.asList(commentId)).get(0);
    }

    public List<Comment> listComment(List<Long> commentIds) {
        List<String> keys = commentIds.stream().map(this::getCommentKey).collect(Collectors.toList());
        List<Map<String, Object>> list = redisUtil.hMultiEntries(keys);
        List<Comment> comments = list.stream().map(self::mapToComment).collect(Collectors.toList());
        //查询缓存中不存在的评论
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i) == null) {
                Long commentId = commentIds.get(i);
                //更新缓存采用分布式锁，防止数据库被击穿
                comments.set(i, self.updateCache(commentId));
                if (comments.get(i) == null) {
                    //高并发下可能超拿不到锁，会返回null，此时直接去查找缓存
                    comments.set(i, self.mapToComment(redisUtil.entries(getCommentKey(commentId))));
                }
            }
        }
        return comments;
    }

    @Lock(prefix = "updateCommentCache", key = "#commentId",
            waitTime = 5,
            waitStrategy = LockWaitStrategyEnum.TIME_WAIT,
            failStrategy = LockFailStrategyEnum.RETURN_DEFAULT)
    protected Comment updateCache(Long commentId) {
        String commentKey = getCommentKey(commentId);
        Comment cacheComment = self.mapToComment(redisUtil.entries(commentKey));
        if (cacheComment != null)
            return cacheComment;

        Comment comment = baseMapper.selectById(commentId);
        redisUtil.hPutAll(commentKey, JsonUtils.toMap(comment));
        redisUtil.expire(commentKey, COMMENT_EXPIRE, COMMENT_EXPIRE_UNIT);
        return comment;
    }

    public void refreshComment(ResourceDTO resourceDTO) {
        List<Comment> comments = baseMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getResourceType, resourceDTO.getType())
                .eq(Comment::getResourceId, resourceDTO.getId())
        );
        self.cacheComment(comments);
    }

    public List<Comment> listComment(ResourceDTO resourceDTO, CommentOrderEnum orderBy, Long start, Long end) {
        List<Long> ids = self.listCommentId(resourceDTO, orderBy, start, end);
        return listComment(ids);
    }

    public long countComment(ResourceDTO dto) {
        return redisUtil.zSize(getCommentKey(dto, CommentOrderEnum.HOT));
    }

    public List<Long> listCommentId(ResourceDTO resourceDTO, CommentOrderEnum orderBy, Long start, Long end) {
        String commentOrderKey = getCommentKey(resourceDTO, orderBy);
        return CollectionUtils.intToLong(redisUtil.zReverseRange(commentOrderKey, start, end));
    }

    private Comment mapToComment(Map<String, Object> map) {
        if (map.get(Comment.Fields.id) == null)
            return null;
        return JsonUtils.parse(map, Comment.class);
    }

    protected String getCommentKey(Long commentId) {
        return RedisUtil.getSimpleCacheKey(ResourceTypeEnum.COMMENT, commentId);
    }

    private String getCommentKey(ResourceDTO dto, CommentOrderEnum orderBy) {
        return RedisUtil.getSimpleCacheKey("comment", dto.getType(), dto.getId(), orderBy);
    }

    private void updateCacheField(Long commentId, String fieldName, Object newVal) {
        String commentKey = getCommentKey(commentId);
        redisUtil.hPut(commentKey, fieldName, newVal);
    }
}
