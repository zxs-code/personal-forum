package com.github.code.zxs.core.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cursor {
    private Long total;
    private Boolean isBegin;
    private Boolean isEnd;
    private Long prev;
    private Long next;
}
