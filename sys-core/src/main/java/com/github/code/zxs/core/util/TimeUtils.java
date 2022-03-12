package com.github.code.zxs.core.util;

import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtils {

    public static Date ZonedDateTimeToDate(ZonedDateTime dateTime){
        return Date.from(dateTime.toInstant());
    }
}
