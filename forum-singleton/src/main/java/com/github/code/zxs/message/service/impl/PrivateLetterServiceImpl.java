package com.github.code.zxs.message.service.impl;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.Cursor;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.message.manager.SessionManager;
import com.github.code.zxs.message.manager.PrivateLetterManager;
import com.github.code.zxs.message.model.bo.PrivateLetterBO;
import com.github.code.zxs.message.model.bo.SessionBO;
import com.github.code.zxs.message.model.entity.PrivateLetter;
import com.github.code.zxs.message.model.entity.Session;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.message.service.PrivateLetterService;
import com.github.code.zxs.resource.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrivateLetterServiceImpl implements PrivateLetterService {
    private final static long PAGE_SIZE = 20;

    @Autowired
    private UserClient userClient;
    @Autowired
    private PrivateLetterManager privateLetterManager;
    @Autowired
    private SessionManager sessionManager;

    @Override
    public RangeResult<SessionBO> rangeSession(long next, boolean reverse) {
        Long userId = UserContext.getId();
        PageDTO pageDTO = PageDTO.ofRange(next, PAGE_SIZE, reverse);

        List<Session> sessions = sessionManager.rangeSession(userId, pageDTO);
        List<SessionBO> sessionBOs = new ArrayList<>();
        for (Session session : sessions) {
            SessionBO sessionBO = new SessionBO();
            sessionBO.setId(session.getId());
            long talkerId = session.getUser1().equals(userId) ? session.getUser2() : session.getUser1();
            sessionBO.setTalker(userClient.getAuthor(talkerId));
            sessionBO.setLastMessage(entity2BO(privateLetterManager.lastMessage(session.getId()), userId));
            sessionBO.setUnreadCount(privateLetterManager.count(session.getId(), MessageStatus.UNREAD));
            sessionBOs.add(sessionBO);
        }
        return new RangeResult<>(Cursor.of(pageDTO.getStart(), pageDTO.getEnd(), sessionBOs.size() < PAGE_SIZE), sessionBOs);
    }

    @Override
    public RangeResult<PrivateLetterBO> rangeMessage(long sessionId, long next, boolean reverse) {
        checkSession(sessionId);
        long userId = UserContext.getId();
        PageDTO pageDTO = PageDTO.ofRange(next, PAGE_SIZE, reverse);

        List<PrivateLetter> messages = privateLetterManager.rangeMessage(sessionId, pageDTO);
        List<PrivateLetterBO> collect = messages.stream().map(message -> entity2BO(message, userId)).collect(Collectors.toList());
        return new RangeResult<>(Cursor.of(pageDTO.getStart(), pageDTO.getEnd(), collect.size() < PAGE_SIZE), collect);
    }

    @Override
    public boolean deleteMessage(long messageId) {
        checkSenderOrReceiver(messageId);
        return privateLetterManager.updateByMessageId(messageId, MessageStatus.DELETED);
    }

    @Override
    public boolean deleteSession(long sessionId) {
        checkSession(sessionId);
        sessionManager.delete(sessionId);
        return privateLetterManager.updateBySessionId(sessionId, MessageStatus.DELETED);
    }

    @Override
    public boolean recallMessage(long messageId) {
        checkSender(messageId);
        return privateLetterManager.recallMessage(messageId);
    }

    private void checkSenderOrReceiver(long messageId) {
        long userId = UserContext.getId();
        PrivateLetter message = privateLetterManager.getMessage(messageId, PrivateLetter::getSender, PrivateLetter::getReceiver);
        if (userId != message.getSender() && userId != message.getReceiver())
            throw new UserEventException("不是消息的发送者和接收者", userId);
    }

    private void checkSender(long messageId) {
        long userId = UserContext.getId();
        PrivateLetter message = privateLetterManager.getMessage(messageId, PrivateLetter::getSender);
        if (userId != message.getSender())
            throw new UserEventException("不是消息的发送者", userId);
    }

    private void checkSession(long sessionId) {
        long userId = UserContext.getId();
        Session session = sessionManager.getSession(sessionId, Session::getUser1, Session::getUser2);
        if (userId != session.getUser1() && userId != session.getUser2())
            throw new UserEventException("会话不存在", userId);
    }

    private PrivateLetterBO entity2BO(PrivateLetter privateLetter, long userId) {
        if (privateLetter.getReceiverStatus() == MessageStatus.RECALL && privateLetter.getReceiver() == userId) {
            return PrivateLetterBO.builder()
                    .messageStatus(privateLetter.getReceiverStatus())
                    .build();
        }
        return PrivateLetterBO.builder()
                .id(privateLetter.getId())
                .content(privateLetter.getContent())
                .sender(privateLetter.getSender())
                .receiver(privateLetter.getReceiver())
                .messageStatus(privateLetter.getReceiver() == userId ? privateLetter.getReceiverStatus() : privateLetter.getSenderStatus())
                .sendTime(privateLetter.getSendTime())
                .build();
    }

}
