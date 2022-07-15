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

    public static Cursor of(Long start, Long end, Boolean isEnd, Long total) {
        Cursor cursor = new Cursor();
        cursor.setTotal(total);
        cursor.setIsBegin(start == 0);
        cursor.setIsEnd(isEnd);
        cursor.setPrev(start - 1);
        cursor.setNext(end + 1);
        return cursor;
    }

    public static Cursor of(Long start, Long end, Boolean isEnd) {
        return of(start, end, isEnd, null);
    }


    public static Cursor of(Long start, Long end) {
        return of(start, end, null, null);
    }
}
