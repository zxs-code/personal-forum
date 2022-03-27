package com.github.code.zxs.core.component;







@Data
@AllArgsConstructor
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

    public PageResult(Long curPage, Long pageSize, Long total, Long pages) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
    }

    public static <T> PageResult<T> getPageResult(Page<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.getRecords());
    }

    public static <T> PageResult<T> getPageResult(Page page, List<T> item) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), item);
    }
}
