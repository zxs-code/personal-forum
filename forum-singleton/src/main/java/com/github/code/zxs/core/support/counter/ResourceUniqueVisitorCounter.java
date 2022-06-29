package com.github.code.zxs.core.support.counter;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

public class ResourceUniqueVisitorCounter {

    private RedisUniqueVisitorCounter uvCounter;
    private ResourceTypeEnum type;

    public ResourceUniqueVisitorCounter(RedisTemplate<String, Object> redisTemplate, ResourceTypeEnum type) {
        uvCounter = new RedisUniqueVisitorCounter(redisTemplate);
        this.type = type;
    }

    public long get(Long resourceId) {
        return uvCounter.get(getKey(resourceId));
    }

    public boolean incr(Long resourceId, Object... userId) {
        return uvCounter.incr(getKey(resourceId),userId);
    }

    public boolean reset(Long resourceId) {
        return uvCounter.reset(getKey(resourceId)) == 1;
    }

    private String getKey(Long resourceId){
        return StringUtils.join(":","uv_counter",type,resourceId);
    }
}
