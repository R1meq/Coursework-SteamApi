package com.example.coursework.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalDateGetter {
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
