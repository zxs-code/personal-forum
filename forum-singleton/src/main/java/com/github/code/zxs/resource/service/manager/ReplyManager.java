package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.model.enums.LockFailStrategyEnum;
import com.github.code.zxs.core.model.enums.LockWaitStrategyEnum;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.resource.mapper.ReplyMapper;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Reply;
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
public class ReplyManager extends ServiceImpl<ReplyMapper, Reply> {
    public static final long REPLY_EXPIRE = 7;
    public static final TimeUnit REPLY_EXPIRE_UNIT = TimeUnit.DAYS;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ReplyManager self;

    public void refreshReply(ResourceDTO resourceDTO) {
        List<Reply> replies = baseMapper.selectList(new LambdaQueryWrapper<Reply>()
                .eq(Reply::getResourceType, resourceDTO.getType())
                .eq(Reply::getResourceId, resourceDTO.getId())
        );
        self.cacheReply(replies);
    }

    public void updateLikeOrDislikeCount(List<Reply> replies) {
        //更新热评中的点赞数
        for (Reply reply : replies) {
            Long replyId = reply.getId();
            Long likeCount = reply.getLikes();
            Long dislikeCount = reply.getLikes();
            if (likeCount != null) {
                //更新评论实体的点赞数
                updateCacheField(replyId, Reply.Fields.likes, likeCount);
            }
            if (dislikeCount != null) {
                //更新评论实体的点踩数
                updateCacheField(replyId, Reply.Fields.dislikes, dislikeCount);
            }
        }
        //更新数据库
        self.updateBatchById(replies);
    }

    private void updateCacheField(Long replyId, String fieldName, Object newVal) {
        String replyKey = getReplyKey(replyId);
        redisUtil.hPut(replyKey, fieldName, newVal);
    }


    public void persistReply(Reply reply) {
        self.save(reply);
    }

    public void persistReply(List<Reply> replies) {
        self.saveBatch(replies);
    }

    public void cacheReply(List<Reply> replies) {
        //添加过期策略 TODO
        for (Reply reply : replies) {
            Long commentId = reply.getCommentId();
            ResourceDTO dto = new ResourceDTO(reply.getResourceId(), reply.getResourceType());
            redisUtil.executePipelined(new SessionCallback() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    String replyKey = getReplyKey(reply.getId());
                    operations.opsForHash().putAll(replyKey, JsonUtils.toMap(reply));
                    operations.expire(replyKey, REPLY_EXPIRE, REPLY_EXPIRE_UNIT);
                    operations.opsForZSet().add(getReplyKey(commentId, CommentOrderEnum.TIME), reply.getId(), reply.getSeq());
                    operations.opsForValue().increment(getResourceReplyCountKey(dto));
                    if (reply.getToUserId() != null) {
                        String dialogKey = getDialogKey(reply.getDialogId());
                        operations.opsForZSet().add(dialogKey, reply.getDialogId(), -1);
                        operations.opsForZSet().add(dialogKey, reply.getId(), reply.getSeq());
                    }
                    return null;
                }
            });
        }
    }

    public List<Reply> listReply(List<Long> replyIds) {
        List<String> keys = replyIds.stream().map(this::getReplyKey).collect(Collectors.toList());
        List<Map<String, Object>> list = redisUtil.hMultiEntries(keys);
        List<Reply> replies = list.stream().map(self::mapToReply).collect(Collectors.toList());
        //查询缓存中不存在的回复
        for (int i = 0; i < replies.size(); i++) {
            if (replies.get(i) == null) {
                Long replyId = replyIds.get(i);
                //更新缓存采用分布式锁，防止数据库被击穿
                replies.set(i, self.updateCache(replyId));
                if (replies.get(i) == null) {
                    //高并发下可能超拿不到锁，会返回null，此时直接去查找缓存
                    replies.set(i, self.mapToReply(redisUtil.entries(getReplyKey(replyId))));
                }
            }
        }
        return replies;
    }

    public List<Reply> listReply(Long commentId, Long start, Long end) {
        List<Long> ids = self.listReplyIdByTime(commentId, start, end);
        return self.listReply(ids);
    }

    public long countReply(Long commentId) {
        return redisUtil.zSize(getReplyKey(commentId, CommentOrderEnum.TIME));
    }

    public long countReply(ResourceDTO resourceDTO) {
        Object count = redisUtil.get(getResourceReplyCountKey(resourceDTO));
        return count == null ? 0 : (int) count;
    }

    public List<Long> countReply(List<Long> commentIds) {
        List<String> keyList = commentIds.stream().map(e -> getReplyKey(e, CommentOrderEnum.TIME)).collect(Collectors.toList());
        return redisUtil.zMultiSize(keyList);
    }

    public List<Long> listReplyIdByTime(Long commentId, Long start, Long end) {
        String replyOrderKey = getReplyKey(commentId, CommentOrderEnum.TIME);
        return CollectionUtils.intToLong(redisUtil.zRange(replyOrderKey, start, end));
    }

    public List<Long> listReplyIdByDialogId(Long dialogId, Long start, Long end) {
        String dialogKey = getDialogKey(dialogId);
        return CollectionUtils.intToLong(redisUtil.zRange(dialogKey, start, end));
    }

    @Lock(prefix = "updateReplyCache", key = "#replyId",
            waitTime = 5,
            waitStrategy = LockWaitStrategyEnum.TIME_WAIT,
            failStrategy = LockFailStrategyEnum.RETURN_DEFAULT)
    protected Reply updateCache(Long replyId) {
        String replyKey = getReplyKey(replyId);
        Reply cacheReply = self.mapToReply(redisUtil.entries(replyKey));
        if (cacheReply != null)
            return cacheReply;

        Reply reply = baseMapper.selectById(replyId);
        redisUtil.hPutAll(replyKey, JsonUtils.toMap(reply));
        redisUtil.expire(replyKey, REPLY_EXPIRE, REPLY_EXPIRE_UNIT);
        return reply;
    }


    private String getReplyKey(Long replyId) {
        return RedisUtil.getSimpleCacheKey("reply", replyId);
    }

    private String getDialogKey(Long dialogId) {
        return RedisUtil.getSimpleCacheKey("dialog", dialogId);
    }

    private String getReplyKey(Long commentId, CommentOrderEnum orderBy) {
        return RedisUtil.getSimpleCacheKey("reply", commentId, orderBy);
    }

    private String getResourceReplyCountKey(ResourceDTO dto) {
        return RedisUtil.getSimpleCacheKey("resourceReplyCount", dto.getType(), dto.getId());
    }

    private Reply mapToReply(Map<String, Object> map) {
        if (map.get(Reply.Fields.id) == null)
            return null;
        return JsonUtils.parse(map, Reply.class);
    }

    public Long countDialogReply(long dialogId) {
        return redisUtil.zSize(getDialogKey(dialogId));
    }


}
