package com.github.code.zxs.search.service.biz.impl;

import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.SearchUtils;
import com.github.code.zxs.search.factory.*;
import com.github.code.zxs.search.model.bean.Searchable;
import com.github.code.zxs.search.model.dto.SearchDTO;
import com.github.code.zxs.search.service.biz.base.SearchService;
import com.github.code.zxs.search.service.manager.SearchManager;
import com.github.code.zxs.search.strategy.wrap.ItemWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchManager searchManager;

    public PageResult<?> search(SearchDTO searchDTO) {
        PageDTO pageDTO = PageDTO.ofPage(searchDTO.getCurPage(), searchDTO.getPageSize());
        ResourceTypeEnum type = searchDTO.getType();
        PageResult<? extends Searchable> pageResult = searchManager.pageMultiMatchQuery(
                pageDTO,
                SearchUtils.getDocumentClass(type),
                FilterFactory.getFilter(type, searchDTO.getFilter()),
                SortFactory.getSort(type, searchDTO.getOrder()),
                SourceFilterFactory.getSourceFilter(type),
                searchDTO.getKeyword(),
                MatchFieldFactory.getMatchField(type)
        );
        List<? extends Searchable> item = pageResult.getItem();
        ItemWrapper itemWrapper = ItemWrapperFactory.getItemWrapper(type);
        if (itemWrapper != null) {
            List<Object> collect = item.stream().map(itemWrapper::wrap).collect(Collectors.toList());
            return PageResult.of(pageResult, collect);
        }
        return pageResult;
    }
}
