package com.github.code.zxs.core.user.mapper;





public interface UserMapper extends BaseMapper<User> {
    void selectOne(QueryWrapper<String> wrapper);
}
