package com.github.code.zxs.message.model.bo;

import com.github.code.zxs.resource.model.bo.AuthorBO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LikeItem {
    private Long id;
    private AuthorBO author;
    private Date time;
}
