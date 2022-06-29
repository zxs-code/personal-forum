package com.github.code.zxs.resource.service.biz.impl;


import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.auth.model.bo.UserBO;
import com.github.code.zxs.auth.service.AuthService;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.UUIDUtils;
import com.github.code.zxs.resource.converter.UserInfoConverter;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.UserInfoBO;
import com.github.code.zxs.resource.model.dto.UserInfoSaveDTO;
import com.github.code.zxs.resource.model.entity.UserInfo;
import com.github.code.zxs.resource.model.enums.GenderEnum;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import com.github.code.zxs.resource.service.manager.UserInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "user")
public class UserInfoServiceImpl implements UserInfoService {
    // Feign TODO
    @Autowired
    private AuthService authService;
    @Autowired
    private UserInfoManager userInfoManager;
    @Autowired
    private UserInfoConverter userInfoConverter;

    @Override
    public AuthorBO getAuthorBoById(Long id) {
        return userInfoConverter.entity2AuthorBo(userInfoManager.getUserInfoById(id));
    }

    @Override
    public List<AuthorBO> listAuthorBo(List<Long> ids) {
        return userInfoManager.listUserInfo(ids).stream().map(userInfoConverter::entity2AuthorBo).collect(Collectors.toList());
    }

    @Override
    public UserInfoBO getMyInfo() {
        Long userId = UserContext.getId();
        UserInfo userInfo = userInfoManager.getUserInfoById(userId);
        if (userInfo == null)
            userInfo = insertDefaultUserInfo(userId);
        UserBO userBO = authService.getUserBO(userId);

        UserInfoBO userInfoBO = userInfoConverter.entityToBo(userInfo, userBO);

        userInfoBO.setExpireAt(authService.loginExpireAt(userId));
        return userInfoBO;
    }

    @Override
    public void saveMyInfo(UserInfoSaveDTO userInfoSaveDTO) {
        UserInfo info = BeanUtils.copy(userInfoSaveDTO, new UserInfo());
        info.setId(UserContext.getId());
        userInfoManager.updateUser(info);
    }

    private UserInfo getDefaultUserInfo(Long id) {
        return UserInfo.builder()
                .id(id)
                .nickname("用户" + UUIDUtils.randomUUID(14))
                .gender(GenderEnum.SECRET).build();
    }

    private UserInfo insertDefaultUserInfo(Long id) {
        UserInfo defaultUserInfo = getDefaultUserInfo(id);
        userInfoManager.addUserInfo(defaultUserInfo);
        return defaultUserInfo;
    }
}
