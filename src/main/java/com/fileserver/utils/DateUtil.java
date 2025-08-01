package com.fileserver.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String formatDate(final String format) {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern(format);
        return formatDate.format(LocalDateTime.now());
    }
}
