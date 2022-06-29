package com.github.code.zxs.resource.converter;


import com.github.code.zxs.auth.model.bo.UserBO;
import com.github.code.zxs.auth.model.vo.UserLoginDTO;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.SimpleUserInfoBO;
import com.github.code.zxs.resource.model.bo.UserDetailBO;
import com.github.code.zxs.resource.model.bo.UserInfoBO;
import com.github.code.zxs.resource.model.dto.UserDTO;
import com.github.code.zxs.resource.model.entity.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverter {
    public UserInfoBO entityToBo(UserInfo userInfo, UserBO userBO) {
        return UserInfoBO.builder()
                .id(userInfo.getId())
                .nickname(userInfo.getNickname())
                .birthday(userInfo.getBirthday())
                .avatar(userInfo.getAvatar())
                .gender(userInfo.getGender())
                .signature(userInfo.getSignature())
                .username(userBO.getUsername())
                .phone(userBO.getPhone())
                .email(userBO.getEmail())
                .state(userBO.getState())
                .createTime(userBO.getCreateTime())
                .updateTime(userBO.getUpdateTime())
                .build()
                ;
    }

    public AuthorBO entity2AuthorBo(UserInfo userInfo) {
        return BeanUtils.copy(userInfo, new AuthorBO());
    }


    public UserDetailBO entity2DetailBo(UserInfo userInfo) {
        return BeanUtils.copy(userInfo, new UserDetailBO());
    }


    public UserInfo dtoEntity(UserDTO userDTO) {
        return BeanUtils.copy(userDTO, new UserInfo());
    }

    public SimpleUserInfoBO dto2SimpleBo(UserDTO userDTO) {
        return BeanUtils.copy(userDTO, new SimpleUserInfoBO());
    }

    public UserDTO vo2Dto(UserLoginDTO userLoginDTO) {
        return BeanUtils.copy(userLoginDTO, new UserDTO());
    }

}
