package com.github.code.zxs.resource.client;

import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserClient {
    @Autowired
    private UserInfoService userInfoService;


    public AuthorBO getAuthor(Long id) {
        return userInfoService.getAuthorBoById(id);
    }
}
