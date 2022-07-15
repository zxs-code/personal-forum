package com.github.code.zxs.message.manager.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.config.MybatisPlusConfig;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.SqlUtils;
import com.github.code.zxs.message.manager.RemindManager;
import com.github.code.zxs.message.mapper.RemindMapper;
import com.github.code.zxs.message.model.entity.Remind;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RemindManagerImpl extends ServiceImpl<RemindMapper, Remind> implements RemindManager {

    private static final String KEY = "remind";
    @Autowired
    private RemindManagerImpl self;

    @Autowired
    private DistributedIdGenerator idGenerator;

    @CreateCache
    private Cache<Long, Remind> remindCache;


    @Override
    public Remind getRemind(ResourceDTO resourceDTO, ActionEnum action, SFunction<Remind, ?>... columns) {
        return getOne(lambdaQuery()
                .select(columns)
                .eq(Remind::getAction, action)
                .eq(Remind::getResourceType, resourceDTO.getType())
                .eq(Remind::getResourceId, resourceDTO.getId()));
    }

    @Override
    public Remind getRemind(Long remindId, SFunction<Remind, ?>... columns) {
        return getOne(lambdaQuery()
                .select(columns)
                .eq(Remind::getId, remindId)
        );
    }


    @Cached(name = "remind", key = "#id", cacheType = CacheType.BOTH)
    @CachePenetrationProtect
    @CacheRefresh(refresh = 20, stopRefreshAfterLastAccess = 60)
    @Override
    public Remind get(Long id) {
        return self.get(id);
    }

    @Override
    public List<Remind> getRemindByReceiver(Long receiverId, PageDTO pageDTO) {
        if (pageDTO.getStart() > pageDTO.getEnd())
            return CollectionUtils.emptyList();
        List<Long> ids = self.listObjs(Wrappers.lambdaQuery(Remind.class)
                        .select(Remind::getId)
                        .eq(Remind::getReceiver, receiverId)
                        .notIn(Remind::getStatus, MessageStatus.DELETED, MessageStatus.IGNORE)
                        .orderByDesc(Remind::getRemindTime)
                        .last(SqlUtils.generateLimit(pageDTO))
                ,
                Long.class::cast
        );
        Map<Long, Remind> all = getAll(ids);
        return ids.stream().map(all::get).collect(Collectors.toList());
    }

    @Override
    public boolean update(Remind remind) {
        return saveWithId(remind);
    }

    @CacheUpdate(name = "remind", key = "#remind.id", value = "#remind")
    public boolean saveWithId(Remind remind) {
        return super.saveOrUpdate(remind);
    }


    @Override
    public boolean insert(Remind remind) {
        remind.setId(idGenerator.getLongId(KEY));
        return self.saveWithId(remind);
    }

    @Override
    public boolean delete(Long remindId) {
        return self.update(Remind.builder().id(remindId).status(MessageStatus.DELETED).build());
    }

    @Override
    public boolean ignore(Long remindId) {
        return self.update(Remind.builder().id(remindId).status(MessageStatus.IGNORE).build());
    }

    @Override
    public long count(long userId, MessageStatus status) {
        return lambdaQuery()
                .eq(Remind::getReceiver, userId)
                .eq(Remind::getStatus, status)
                .count();
    }


    @Override
    public Map<Long, Remind> getAll(List<Long> ids) {
        Map<Long, Remind> cached = remindCache.getAll(new HashSet<>(ids));
        Map<Long, Remind> uncached = cached.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .collect(Collectors.collectingAndThen(Collectors.toList(), self::getFromDatabase));

        if (CollectionUtils.isNotEmpty(uncached)) {
            remindCache.putAll(uncached);
            cached.putAll(uncached);
        }
        return cached;
    }

    private Map<Long, Remind> getFromDatabase(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return null;

        Map<Long, Remind> map = new HashMap<>();
        List<Remind> reminds = self.listByIds(ids);
        reminds.forEach(remind -> map.put(remind.getId(), remind));
        return map;
    }


    public static void setActionType(ActionEnum action) {
        String tableName = getTableName();
        MybatisPlusConfig.setDynamicTableName(tableName, processTableName(tableName, action));
    }

    public static void removeActionType() {
        String tableName = getTableName();
        MybatisPlusConfig.removeDynamicTableName(tableName);
    }

    private static String getTableName() {
        return SqlHelper.table(Remind.class).getTableName();
    }

    private static String processTableName(String tableName, ActionEnum actionEnum) {
        return tableName + "_" + actionEnum.getValue();
    }
}
