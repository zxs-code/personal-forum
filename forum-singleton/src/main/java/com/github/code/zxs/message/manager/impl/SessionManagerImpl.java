package com.github.code.zxs.message.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.util.SqlUtils;
import com.github.code.zxs.message.manager.SessionManager;
import com.github.code.zxs.message.mapper.SessionMapper;
import com.github.code.zxs.message.model.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionManagerImpl extends ServiceImpl<SessionMapper, Session> implements SessionManager {
    @Autowired
    private SessionManagerImpl self;

    @Override
    public List<Session> rangeSession(Long userId, PageDTO pageDTO) {
//        return self.list(Wrappers.<Session>lambdaQuery()
//                .eq(Session::getUser1, userId)
//                .or()
//                .eq(Session::getUser2, userId)
//                .orderByDesc(Session::getLastTime)
//                .last(SqlUtils.generateLimit(pageDTO)));
        return lambdaQuery()
                .eq(Session::getUser1, userId)
                .or()
                .eq(Session::getUser2, userId)
                .orderByDesc(Session::getLastTime)
                .last(SqlUtils.generateLimit(pageDTO))
                .list();
    }

    @Scheduled(fixedRate = 10000)
    public void test() {
        self.rangeSession(1L,PageDTO.ofRange(0,10));
    }

    @Override
    public Session getSession(long sessionId, SFunction<Session, ?>... columns) {
        return lambdaQuery()
                .select(columns)
                .eq(Session::getId, sessionId)
                .one();
    }

    @Override
    public boolean delete(long sessionId) {
        return self.delete(sessionId);
    }

}
