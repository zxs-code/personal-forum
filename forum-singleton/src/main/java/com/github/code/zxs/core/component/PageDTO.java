package com.github.code.zxs.core.component;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PageDTO {
    /**
     * 当前页
     */
    private Long curPage = 1L;
    /**
     * 每页大小
     */
    private Long pageSize = 10L;

    private Long start;
    private Long end;

    public static final PageDTO one10 = new PageDTO(1L, 10L);
    public static final PageDTO one15 = new PageDTO(1L, 15L);
    public static final PageDTO one20 = new PageDTO(1L, 20L);

    private PageDTO(Long curPage, Long pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.start = (curPage - 1) * pageSize;
        this.end = curPage * pageSize - 1;
    }

    public static PageDTO ofPage(Long curPage, Long pageSize) {

        if (curPage == 1 && pageSize < Integer.MAX_VALUE && pageSize > 0)
            switch (pageSize.intValue()) {
                case 10:
                    return one10;
                case 15:
                    return one15;
                case 20:
                    return one20;
            }
        if (curPage <= 0 || pageSize <= 0)
            log.debug("页面参数可能异常curPage:{},pageSize:{}", curPage, pageSize);
        return new PageDTO(curPage <= 0 ? 1 : curPage, pageSize <= 0 ? 10 : pageSize);
    }

}
