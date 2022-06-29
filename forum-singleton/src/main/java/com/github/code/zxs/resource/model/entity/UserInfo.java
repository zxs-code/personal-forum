package com.github.code.zxs.resource.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseLogicEntity;
import com.github.code.zxs.resource.model.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_info")
public class UserInfo extends BaseLogicEntity {
    private Long id;
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
