package com.github.code.zxs.message.model.bo;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import lombok.Data;

import java.util.Date;

@Data
public class RemindBO {
    private Long id;
    private ResourceTypeEnum resourceType;
    private Long resourceId;
    private String title;
    private MessageStatus status;
    private AuthorBO sender;
    private Long counts;
    private Date remindTime;
}
