package com.github.code.zxs.message.service.impl;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.Cursor;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.message.manager.RemindManager;
import com.github.code.zxs.message.model.bo.LikeDetail;
import com.github.code.zxs.message.model.bo.LikeItem;
import com.github.code.zxs.message.model.bo.RemindBO;
import com.github.code.zxs.message.model.entity.Remind;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.message.service.RemindService;
import com.github.code.zxs.resource.client.UserActionClient;
import com.github.code.zxs.resource.client.UserClient;
import com.github.code.zxs.resource.model.bo.UserActionBO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemindServiceImpl implements RemindService {
    private final static long PAGE_SIZE = 20;
    @Autowired
    private RemindManager remindManager;
    @Autowired
    private UserClient userClient;
    @Autowired
    private UserActionClient userActionClient;

    @Override
    public RangeResult<RemindBO> rangeRemind(long next, boolean reverse) {
        PageDTO pageDTO = PageDTO.ofRange(next, PAGE_SIZE, reverse);
        List<Remind> reminds = remindManager.getRemindByReceiver(UserContext.getId(), pageDTO);

        List<RemindBO> collect = reminds.stream().map(remind -> {
            RemindBO remindBO = entity2BO(remind);
            remindBO.setSender(userClient.getAuthor(remind.getSender()));
            return remindBO;
        }).collect(Collectors.toList());

        Cursor cursor = Cursor.of(pageDTO.getStart(), pageDTO.getEnd(), collect.size() < PAGE_SIZE);
        return new RangeResult<>(cursor, collect);
    }

    @Override
    public void delete(long remindId) {
        auth(remindId);
        remindManager.delete(remindId);
    }

    @Override
    public void ignore(long remindId) {
        auth(remindId);
        remindManager.ignore(remindId);
    }

    @Override
    public long unreadCount() {
        Long userId = UserContext.getId();
        return remindManager.count(userId, MessageStatus.UNREAD);
    }


    public LikeDetail likeDetail(long remindId, long next, boolean reverse) {
        auth(remindId);
        PageDTO pageDTO = PageDTO.ofRange(next, PAGE_SIZE, reverse);
        Remind remind = remindManager.getRemind(remindId);
        ResourceDTO resourceDTO = new ResourceDTO(remind.getResourceId(), remind.getResourceType());
        RangeResult<UserActionBO> rangeResult = userActionClient.listAction(resourceDTO, ActionEnum.LIKE, pageDTO);
        LikeDetail likeDetail = new LikeDetail();
        likeDetail.setCursor(rangeResult.getCursor());
        likeDetail.setItem(rangeResult.getItem().stream().map(this::action2Item).collect(Collectors.toList()));
        if (pageDTO.getStart() == 0) {
            likeDetail.setRemind(remind);
        }
        return likeDetail;
    }

    private LikeItem action2Item(UserActionBO action) {
        return new LikeItem(action.getId(), action.getAuthor(), action.getTime());
    }

    private void auth(long remindId) {
        Long userId = UserContext.getId();
        Remind remind = remindManager.getRemind(remindId, Remind::getReceiver);
        if (!userId.equals(remind.getReceiver()))
            throw new UserEventException("收件箱中无此提醒", userId);
    }

    private RemindBO entity2BO(Remind remind) {
        return BeanUtils.copy(remind, new RemindBO());
    }
}
