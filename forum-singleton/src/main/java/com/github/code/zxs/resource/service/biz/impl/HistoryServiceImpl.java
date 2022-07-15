package com.github.code.zxs.resource.service.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.resource.mapper.HistoryMapper;
import com.github.code.zxs.resource.model.bo.HistoryBO;
import com.github.code.zxs.resource.model.entity.History;
import com.github.code.zxs.resource.service.biz.base.HistoryService;
import com.github.code.zxs.resource.service.biz.base.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {
    @Autowired
    private PostsService postsService;

    @Override
    public PageResult<HistoryBO> pageHistory(PageDTO pageDTO) {
        Page<History> historyPage = baseMapper.selectPage(Page.of(pageDTO.getCurPage(), pageDTO.getPageSize()),
                new LambdaQueryWrapper<History>()
                        .eq(History::getUserId, UserContext.getId())
                        .orderByDesc(History::getViewTime)
        );
        List<History> records = historyPage.getRecords();
        List<HistoryBO> result = records.stream().map(e -> {
            HistoryBO historyBO = BeanUtils.copy(e, new HistoryBO());
            historyBO.setTitle(postsService.getTitle(historyBO.getResourceId()));
            return historyBO;
        }).collect(Collectors.toList());
        return PageResult.getPageResult(historyPage, result);
    }

    @Override
    public void saveHistory(History history) {
        if (baseMapper.exists(new LambdaQueryWrapper<History>()
                .eq(History::getResourceType, history.getResourceType())
                .eq(History::getResourceId, history.getResourceId())
        )) {
            History update = new History();
            update.setViewTime(new Date());
            baseMapper.update(update, new LambdaQueryWrapper<History>()
                    .eq(History::getResourceType, history.getResourceType())
                    .eq(History::getResourceId, history.getResourceId()));
        } else
            baseMapper.insert(history);
    }
}
