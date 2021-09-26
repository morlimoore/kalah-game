package com.ikenna.echendu.kalah.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    public static DateTimeFormatter getDateTimeFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    }

    public static SimpleDateFormat getSimpleDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    }

    public static SimpleDateFormat getJavaTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }
}
