package com.code.zxs.auth.dto;

import com.code.zxs.auth.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenUserDTO {

    public TokenUserDTO(User user){
        id = user.getId();
        createTime = user.getCreateTime();
    }

    private String id;
    private Date createTime;
}
