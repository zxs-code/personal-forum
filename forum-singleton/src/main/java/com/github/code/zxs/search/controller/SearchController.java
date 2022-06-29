package com.github.code.zxs.search.controller;

import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.search.model.dto.SearchDTO;
import com.github.code.zxs.search.service.biz.base.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public PageResult<?> search(@ModelAttribute SearchDTO searchDTO) {
        searchDTO.setPageSize(20L);
        return searchService.search(searchDTO);
    }
}
