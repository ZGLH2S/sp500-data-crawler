package com.albez.sp500.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static Date timestampToDate(long timestamp) {
        return new Date(Math.multiplyExact(timestamp, 1000L));
    }

    public static Date stringToDate(String date) {
        return Date.from(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant());
    }
}
