package com.github.code.zxs.core.util;


import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DateUtils extends DateUtil {

    public static final String BASIC_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final String ZONED_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static Date ZonedDateTimeToDate(ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    public static ZonedDateTime DateToZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime DateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static ZonedDateTime DateToZonedDateTime(Date date, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static Date plus(Date date, long amountToAdd, TimeUnit unit) {
        return plus(date, amountToAdd, toChronoUnit(unit));
    }

    public static Date minus(Date date, long amountToSubtract, TimeUnit unit) {
        return minus(date, amountToSubtract, toChronoUnit(unit));
    }


    public static Date plus(Date date, long amountToAdd, ChronoUnit unit) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date).plus(amountToAdd, unit);
        return ZonedDateTimeToDate(newZDTime);
    }

    public static Date minus(Date date, long amountToSubtract, ChronoUnit unit) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date).minus(amountToSubtract, unit);
        return ZonedDateTimeToDate(newZDTime);
    }

    public static Date with(Date date, ChronoField field, long newValue) {
        ZonedDateTime newZDTime = DateToZonedDateTime(date).with(field, newValue);
        return ZonedDateTimeToDate(newZDTime);
    }

    public static ChronoUnit toChronoUnit(TimeUnit unit) {
        try {
            return ChronoUnit.valueOf(unit.name());
        } catch (IllegalArgumentException e) {
            log.error("不支持TimeUnit {} 转换成ChronoUnit", unit.name(), e);
            throw e;
        }
    }

    public static TimeUnit toTimeUnit(ChronoUnit unit) {
        try {
            return TimeUnit.valueOf(unit.name());
        } catch (IllegalArgumentException e) {
            log.error("不支持ChronoUnit {} 转换成TimeUnit", unit.name(), e);
            throw e;
        }
    }

    /**
     * 获取年月日
     *
     * @param date
     * @param separator 分隔符
     * @return
     */
    public static String date(Date date, String separator) {
        String year = NumberUtils.prefixZero(4, DateUtils.year(date));
        String month = NumberUtils.prefixZero(2, DateUtils.month(date) + 1);
        String dayOfMonth = NumberUtils.prefixZero(2, DateUtils.dayOfMonth(date));
        return StringUtils.join(separator, year, month, dayOfMonth);
    }


    public static Date parseBasicDate(String dateStr) {
        return parse(dateStr, BASIC_DATE);
    }

    public static Date parseZonedDate(String dateStr) {
        return parse(dateStr, ZONED_DATE);
    }

    public static String formatZonedDate(Date date) {
        return format(date, ZONED_DATE);
    }
}
