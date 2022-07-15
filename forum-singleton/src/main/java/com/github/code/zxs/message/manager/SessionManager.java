package com.github.code.zxs.message.manager;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.message.model.entity.Session;

import java.util.List;

public interface SessionManager {
    List<Session> rangeSession(Long userId, PageDTO pageDTO);

    Session getSession(long sessionId, SFunction<Session, ?>... columns);

    boolean delete(long sessionId);
}
