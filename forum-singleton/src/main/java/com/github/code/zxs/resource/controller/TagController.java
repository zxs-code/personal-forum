package com.github.code.zxs.resource.controller;


import com.github.code.zxs.resource.model.bo.TagBO;
import com.github.code.zxs.resource.service.biz.base.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public List<TagBO> listTagBO() {
        return tagService.listHotTag(30);
    }
}
