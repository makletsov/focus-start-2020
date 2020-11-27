package ru.makletsov.focusstart.ex2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
}
