package com.github.code.zxs.resource.service.biz.base;

import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.UserInfoBO;
import com.github.code.zxs.resource.model.dto.UserInfoSaveDTO;

import java.util.List;

public interface UserInfoService {

    AuthorBO getAuthorBoById(Long id);

    List<AuthorBO> listAuthorBo(List<Long> ids);

    UserInfoBO getMyInfo();

    void saveMyInfo(UserInfoSaveDTO userInfoSaveDTO);
}
