package com.github.code.zxs.auth.dto;

import com.github.code.zxs.auth.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class TokenUserDTO {

    public TokenUserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        createTime = user.getCreateTime();
    }

    private String id;
    private String username;
    private Date createTime;

    public TokenUserDTO() {

    }
}
