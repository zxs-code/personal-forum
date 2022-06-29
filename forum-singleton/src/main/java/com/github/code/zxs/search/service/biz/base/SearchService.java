package com.github.code.zxs.search.service.biz.base;

import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.search.model.dto.SearchDTO;

public interface SearchService {
    PageResult<?> search(SearchDTO searchDTO);
}
