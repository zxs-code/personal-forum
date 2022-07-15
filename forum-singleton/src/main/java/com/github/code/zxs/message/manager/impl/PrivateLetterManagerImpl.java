package com.github.code.zxs.message.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.util.SqlUtils;
import com.github.code.zxs.message.manager.PrivateLetterManager;
import com.github.code.zxs.message.mapper.PrivateLetterMapper;
import com.github.code.zxs.message.model.entity.PrivateLetter;
import com.github.code.zxs.message.model.enums.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrivateLetterManagerImpl extends ServiceImpl<PrivateLetterMapper, PrivateLetter> implements PrivateLetterManager {

    @Autowired
    private PrivateLetterManagerImpl self;

    @Override
    public PrivateLetter lastMessage(Long sessionId) {
        return lambdaQuery().orderByDesc(PrivateLetter::getSendTime).last("LIMIT 1").getEntity();
    }

    @Override
    public long count(Long sessionId, MessageStatus status) {
        return lambdaQuery()
                .eq(PrivateLetter::getSessionId, sessionId)
                .eq(PrivateLetter::getReceiverStatus, status)
                .count();
    }

    @Override
    public List<PrivateLetter> rangeMessage(long sessionId, PageDTO pageDTO) {
        return lambdaQuery()
                .eq(PrivateLetter::getSessionId, sessionId)
                .ne(PrivateLetter::getReceiverStatus, MessageStatus.DELETED)
                .orderByDesc(PrivateLetter::getSendTime)
                .last(SqlUtils.generateLimit(pageDTO))
                .list();
    }

    @Override
    public PrivateLetter getMessage(long messageId, SFunction<PrivateLetter, ?>... columns) {
        return lambdaQuery()
                .select(columns)
                .eq(PrivateLetter::getId, messageId)
                .one();
    }

    @Override
    public boolean updateByMessageId(long messageId, MessageStatus status) {
        return lambdaUpdate()
                .set(PrivateLetter::getReceiverStatus, status)
                .eq(PrivateLetter::getId, messageId)
                .update();
    }

    @Override
    public boolean updateBySessionId(long sessionId, MessageStatus status) {
        return lambdaUpdate()
                .set(PrivateLetter::getReceiverStatus, status)
                .eq(PrivateLetter::getSessionId, sessionId)
                .update();
    }

    @Override
    public boolean recallMessage(long messageId) {
        return lambdaUpdate()
                .set(PrivateLetter::getSenderStatus, MessageStatus.RECALL)
                .set(PrivateLetter::getReceiverStatus, MessageStatus.RECALL)
                .eq(PrivateLetter::getId, messageId)
                .update();
    }

}
