package com.github.code.zxs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.auth.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
