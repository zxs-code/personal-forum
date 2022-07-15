package com.github.code.zxs.core.component;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RangeResult<T> {
    private Cursor cursor;
    private List<T> item;
}
