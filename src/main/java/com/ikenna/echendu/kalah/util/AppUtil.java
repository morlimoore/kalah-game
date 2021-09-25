package com.ikenna.echendu.kalah.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class AppUtil {

    private AppUtil(){}

    public static String errorMessage = "";

    public static DateTimeFormatter getDateTimeFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    }

    public static SimpleDateFormat getSimpleDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    }

    public static SimpleDateFormat getJavaTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public static void setErrorMessage(String error) {
        errorMessage = error;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
