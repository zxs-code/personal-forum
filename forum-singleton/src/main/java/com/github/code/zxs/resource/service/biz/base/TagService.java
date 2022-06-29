package com.github.code.zxs.resource.service.biz.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.code.zxs.resource.model.bo.TagBO;
import com.github.code.zxs.resource.model.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    void addTagScore(List<String> tags);

    List<TagBO> listHotTag(int amount);
}
