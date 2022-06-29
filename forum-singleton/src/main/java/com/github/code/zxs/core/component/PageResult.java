package com.github.code.zxs.core.component;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /**
     * 当前页
     */
    private Long curPage;
    /**
     * 每页大小
     */
    private Long pageSize;
    /**
     * 条目数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 数据
     */
    private List<T> item;

    public PageResult(Long curPage, Long pageSize, Long total, List<T> item) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.total = total;
        this.item = item;
        this.pages = (total - 1) / pageSize + 1;
    }

    public PageResult(PageDTO pageDTO, Long total, List<T> item) {
        this.curPage = pageDTO.getCurPage();
        this.pageSize = pageDTO.getPageSize();
        this.total = total;
        this.item = item;
        this.pages = (total - 1) / pageSize + 1;
    }

    public static <T> PageResult<T> emptyPage(PageDTO pageDTO) {
        return new PageResult<>(pageDTO, 0L, null);
    }

    public static <T> PageResult<T> of(PageResult<?> pageResult, List<T> item) {
        return new PageResult<>(pageResult.getCurPage(), pageResult.getPageSize(), pageResult.getTotal(), pageResult.getPages(), item);
    }


    public static <T> PageResult<T> getPageResult(Page<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.getRecords());
    }

    public static <T> PageResult<T> getPageResult(Page page, List<T> item) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), item);
    }
}
