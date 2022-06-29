package com.github.code.zxs.resource.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.code.zxs.resource.model.dto.LikeDTO;
import com.github.code.zxs.resource.service.biz.base.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @SaCheckLogin
    @PutMapping
    public void like(@RequestBody @Valid LikeDTO likeDTO) {
        likeService.asyncLike(likeDTO);
    }
}
