package com.code.zxs.auth.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeUtils {

    public static Date ZonedDateTimeToDate(ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    public static ZonedDateTime DateToZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static ZonedDateTime DateToZonedDateTime(Date date, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(date.toInstant(), zoneId);
    }


    public static Date plus(Date date, long amountToAdd, ChronoUnit unit) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date);
        newZDTime.plus(amountToAdd, unit);
        return ZonedDateTimeToDate(newZDTime);
    }

    public static Date minus(Date date, long amountToAdd, ChronoUnit unit) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date);
        newZDTime.minus(amountToAdd, unit);
        return ZonedDateTimeToDate(newZDTime);
    }

    public static Date with(Date date, ChronoField field, long newValue) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date);
        newZDTime.with(field, newValue);
        return ZonedDateTimeToDate(newZDTime);
    }

}
