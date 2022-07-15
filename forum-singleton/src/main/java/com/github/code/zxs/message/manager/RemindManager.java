package com.github.code.zxs.message.manager;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.message.model.entity.Remind;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.ActionEnum;

import java.util.List;
import java.util.Map;

public interface RemindManager {

    Remind getRemind(ResourceDTO resourceDTO, ActionEnum action, SFunction<Remind, ?>... columns);


    Remind getRemind(Long remindId, SFunction<Remind, ?>... columns);

    Remind get(Long id);

    List<Remind> getRemindByReceiver(Long receiverId, PageDTO pageDTO);

    boolean update(Remind remind);

    boolean insert(Remind remind);

    boolean delete(Long remindId);

    boolean ignore(Long remindId);

    long count(long userId, MessageStatus status);

    Map<Long, Remind> getAll(List<Long> ids);
}
