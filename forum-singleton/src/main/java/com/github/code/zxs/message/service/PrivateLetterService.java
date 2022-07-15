package com.github.code.zxs.message.service;

import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.message.model.bo.PrivateLetterBO;
import com.github.code.zxs.message.model.bo.SessionBO;

public interface PrivateLetterService {
    RangeResult<SessionBO> rangeSession(long next, boolean reverse);

    RangeResult<PrivateLetterBO> rangeMessage(long sessionId, long next, boolean reverse);

    boolean deleteMessage(long messageId);

    boolean deleteSession(long sessionId);

    boolean recallMessage(long messageId);
}
