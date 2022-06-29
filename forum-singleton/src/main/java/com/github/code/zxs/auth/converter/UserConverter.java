package com.github.code.zxs.auth.converter;

import com.github.code.zxs.auth.model.bo.UserBO;
import com.github.code.zxs.auth.model.dto.UserRegisterDTO;
import com.github.code.zxs.auth.model.entity.User;
import com.github.code.zxs.core.util.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 转化器性能优化 TODO ObjectMapper Copyable MapStruct
 */
@Component
public class UserConverter {

    public UserRegisterDTO entityToDto(User user) {
        return BeanUtils.copy(user, new UserRegisterDTO());
    }

    public UserBO entityToBO(User user) {
        return BeanUtils.copy(user, new UserBO());
    }

    public User dtoToEntity(UserRegisterDTO dto) {
        return BeanUtils.copy(dto, new User());
    }
}
