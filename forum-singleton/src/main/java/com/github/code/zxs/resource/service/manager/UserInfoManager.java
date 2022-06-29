package com.github.code.zxs.resource.service.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.RedisUtil;
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
    private RedisUtil redisUtil;
    @Autowired
    private UserInfoManager self;

    public UserInfo getUserInfoById(Long id) {
        String userKey = getUserKey(id);
        UserInfo userInfo = (UserInfo) redisUtil.get(userKey);
        if (userInfo == null)
            userInfo = getUserInfoFromDatabase(id);

        return userInfo;
    }

    public List<UserInfo> listUserInfo(List<Long> ids) {
        List<String> keyList = ids.stream().map(this::getUserKey).collect(Collectors.toList());
        List<UserInfo> infos = CollectionUtils.castToList(redisUtil.multiGet(keyList), UserInfo.class);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i) == null)
                infos.set(i, self.getUserInfoFromDatabase(ids.get(i)));
        }
        return infos;
    }

    public void addUserInfo(UserInfo userInfo) {
        baseMapper.insert(userInfo);
    }

    public void  updateUser(UserInfo userInfo) {
        Long id = userInfo.getId();
        redisUtil.del(getUserKey(id));
        baseMapper.updateById(userInfo);
        redisUtil.del(getUserKey(id));
    }

    public void deleteUser(Long id) {
        redisUtil.del(getUserKey(id));
        baseMapper.deleteById(id);
        redisUtil.del(getUserKey(id));
    }

    public String getUserKey(Long userId) {
        return RedisUtil.getSimpleCacheKey(USER, userId);
    }

    @Lock(prefix = USER, key = "#id")
    private UserInfo getUserInfoFromDatabase(Long id) {
        String userKey = getUserKey(id);
        UserInfo userInfo = (UserInfo) redisUtil.get(userKey);
        if (userInfo != null)
            return userInfo;
        userInfo = baseMapper.selectById(id);
        long expire = 3 * 24 * 60 * 60 + (long) (Math.random() * 60 * 60);
        redisUtil.set(userKey, userInfo, expire, TimeUnit.SECONDS);
        return userInfo;
    }
}
