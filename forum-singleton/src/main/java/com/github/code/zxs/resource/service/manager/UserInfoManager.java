package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.RedisUtils;
import com.github.code.zxs.resource.mapper.UserInfoMapper;
import com.github.code.zxs.resource.model.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class UserInfoManager extends ServiceImpl<UserInfoMapper, UserInfo> {
    private static final String USER = "user";

    @Autowired
    private UserInfoManager self;

    public UserInfo getUserInfoById(Long id) {
        String userKey = getUserKey(id);
        UserInfo userInfo = (UserInfo) RedisUtils.get(userKey);
        if (userInfo == null)
            userInfo = getUserInfoFromDatabase(id);

        return userInfo;
    }

    public List<UserInfo> listUserInfo(List<Long> ids) {
        List<String> keyList = ids.stream().map(this::getUserKey).collect(Collectors.toList());
        List<UserInfo> infos = CollectionUtils.castToList(RedisUtils.multiGet(keyList), UserInfo.class);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i) == null)
                infos.set(i, self.getUserInfoFromDatabase(ids.get(i)));
        }
        return infos;
    }

    public void addUserInfo(UserInfo userInfo) {
        baseMapper.insert(userInfo);
    }

    public void updateUser(UserInfo userInfo) {
        Long id = userInfo.getId();
        RedisUtils.delete(getUserKey(id));
        baseMapper.updateById(userInfo);
        RedisUtils.delete(getUserKey(id));
    }

    public void deleteUser(Long id) {
        RedisUtils.delete(getUserKey(id));
        baseMapper.deleteById(id);
        RedisUtils.delete(getUserKey(id));
    }

    public String getUserKey(Long userId) {
        return RedisUtils.getSimpleCacheKey(USER, userId);
    }

    @Lock(prefix = USER, key = "#id")
    private UserInfo getUserInfoFromDatabase(Long id) {
        String userKey = getUserKey(id);
        UserInfo userInfo = (UserInfo) RedisUtils.get(userKey);
        if (userInfo != null)
            return userInfo;
        userInfo = baseMapper.selectById(id);
        long expire = 3 * 24 * 60 * 60 + (long) (Math.random() * 60 * 60);
        RedisUtils.set(userKey, userInfo, expire, TimeUnit.SECONDS);
        return userInfo;
    }
}
