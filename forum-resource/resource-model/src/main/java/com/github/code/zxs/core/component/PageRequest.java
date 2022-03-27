package com.github.code.zxs.core.component;



@Data
public class PageRequest {
    /**
     * 当前页
     */
    private Long curPage = 1L;
    /**
     * 每页大小
     */
    private Long pageSize = 10L;
}
