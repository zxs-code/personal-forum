package com.github.code.zxs.resource.service.biz.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.resource.model.bo.HistoryBO;
import com.github.code.zxs.resource.model.entity.History;

public interface HistoryService extends IService<History> {
    PageResult<HistoryBO> pageHistory(PageDTO pageDTO);

    void saveHistory(History history);
}
