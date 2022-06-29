package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.model.bo.BaseBO;
import com.github.code.zxs.resource.model.enums.GenderEnum;

import java.util.Date;

public class UserDetailBO extends BaseBO {
    private Long id;
    private String nickname;
    private GenderEnum gender;
    private Date birthday;
    private String signature;
    private String avatar;
}
