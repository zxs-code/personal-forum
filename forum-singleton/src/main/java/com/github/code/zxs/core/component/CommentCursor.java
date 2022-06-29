package com.github.code.zxs.core.component;

import com.github.code.zxs.resource.model.enums.CommentOrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCursor extends Cursor{
    private CommentOrderEnum orderBy;

    public CommentCursor(Long total, Boolean isBegin, Boolean isEnd, Long prev, Long next, CommentOrderEnum orderBy) {
        super(total, isBegin, isEnd, prev, next);
        this.orderBy = orderBy;
    }
}
