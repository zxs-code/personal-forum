package com.github.code.zxs.message.service;

import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.message.model.bo.RemindBO;

public interface RemindService {

    RangeResult<RemindBO> rangeRemind(long next, boolean reverse);

    void delete(long remindId);

    void ignore(long remindId);

    long unreadCount();
}
