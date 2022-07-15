package com.github.code.zxs.core.util;

import com.github.code.zxs.core.component.PageDTO;

public class SqlUtils {
    public static String generateLimit(PageDTO pageDTO) {
        return "LIMIT " + pageDTO.getStart() + "," + (pageDTO.getEnd() - pageDTO.getStart() + 1);
    }
}
