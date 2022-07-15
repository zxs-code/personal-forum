package com.github.code.zxs.resource.client;

import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.resource.model.bo.UserActionBO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import org.springframework.stereotype.Component;

@Component
public class UserActionClient {
    public RangeResult<UserActionBO> listAction(ResourceDTO resourceDTO, ActionEnum action, PageDTO pageDTO){
        return null;
    }
}
