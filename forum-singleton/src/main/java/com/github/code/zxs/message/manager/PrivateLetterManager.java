package com.github.code.zxs.message.manager;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.message.model.entity.PrivateLetter;
import com.github.code.zxs.message.model.enums.MessageStatus;

import java.util.List;

public interface PrivateLetterManager {
    PrivateLetter lastMessage(Long sessionId);

    long count(Long sessionId, MessageStatus status);

    List<PrivateLetter> rangeMessage(long sessionId, PageDTO pageDTO);

    PrivateLetter getMessage(long messageId, SFunction<PrivateLetter, ?>... columns);

    boolean updateByMessageId(long messageId, MessageStatus status);

    boolean updateBySessionId(long sessionId, MessageStatus status);

    boolean recallMessage(long messageId);
}
