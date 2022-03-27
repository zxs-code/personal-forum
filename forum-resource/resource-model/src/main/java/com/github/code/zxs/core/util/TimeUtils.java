package com.github.code.zxs.core.util;










@Slf4j
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

    public static Date plus(Date date, long amountToAdd, TimeUnit unit) {
        return plus(date,amountToAdd,toChronoUnit(unit));
    }

    public static Date minus(Date date, long amountToSubtract, TimeUnit unit) {
        return minus(date,amountToSubtract,toChronoUnit(unit));
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

    public static void main(String[] args) {
        System.out.println(toChronoUnit(TimeUnit.MINUTES));
        System.out.println(toTimeUnit(ChronoUnit.MONTHS));
    }
}
