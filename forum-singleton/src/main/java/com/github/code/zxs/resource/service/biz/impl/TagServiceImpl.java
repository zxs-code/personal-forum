package com.github.code.zxs.resource.service.biz.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.resource.mapper.TagMapper;
import com.github.code.zxs.resource.model.bo.TagBO;
import com.github.code.zxs.resource.model.entity.Tag;
import com.github.code.zxs.resource.service.biz.base.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addTagScore(List<String> tags) {
        for (String tag : tags) {
            redisUtil.zAdd("hotTag", tag, 1);
        }
    }

    @Override
    public List<TagBO> listHotTag(int amount) {
        Set<ZSetOperations.TypedTuple<Object>> hotTag = redisUtil.zReverseRangeWithScore("hotTag", 0, amount - 1);
        List<TagBO> collect = hotTag.stream().map(e -> new TagBO((String) e.getValue(), e.getScore())).collect(Collectors.toList());
        return collect;
    }
}
