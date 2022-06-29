package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.resource.model.enums.GenderEnum;
import lombok.Data;

import java.util.Date;
@Data
public class UserInfoSaveDTO {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private GenderEnum gender;
    /**
     * 出生年月
     */
    private Date birthday;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 头像url
     */
    private String avatar;
}
