package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import lombok.Data;

import java.util.Date;

@Data
public class UserActionBO {
    private Long id;
    private AuthorBO author;
    private ActionEnum action;
    private Date time;
    private ResourceDTO resourceDTO;
}
