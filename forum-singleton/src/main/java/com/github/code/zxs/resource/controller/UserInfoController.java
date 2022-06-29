package com.github.code.zxs.resource.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.resource.model.bo.HistoryBO;
import com.github.code.zxs.resource.model.bo.UserInfoBO;
import com.github.code.zxs.resource.model.dto.UserInfoSaveDTO;
import com.github.code.zxs.resource.model.entity.History;
import com.github.code.zxs.resource.service.biz.base.HistoryService;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HistoryService historyService;

    @SaCheckLogin
    @GetMapping("myInfo")
    public UserInfoBO getMyInfo() {
        return userInfoService.getMyInfo();
    }

    @PutMapping("myInfo")
    public void saveMyInfo(@RequestBody UserInfoSaveDTO userInfoSaveDTO) {
        userInfoService.saveMyInfo(userInfoSaveDTO);
    }

    @GetMapping("history")
    public PageResult<HistoryBO> pageHistory(@ModelAttribute PageDTO pageDTO) {
        return historyService.pageHistory(pageDTO);
    }

    @DeleteMapping("history/{id}")
    public void deleteHistory(@PathVariable Long id) {
        historyService.remove(new LambdaQueryWrapper<History>()
                .eq(History::getCreateBy, UserContext.getId())
                .eq(History::getId, id)
        );
    }

    @DeleteMapping("history")
    public void deleteHistory() {
        historyService.remove(new LambdaQueryWrapper<History>()
                .eq(History::getCreateBy, UserContext.getId())
        );
    }
}
