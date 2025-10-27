package com.example.portfolio.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private DateUtils() {}

    public static LocalDate stringToLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String localDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}

