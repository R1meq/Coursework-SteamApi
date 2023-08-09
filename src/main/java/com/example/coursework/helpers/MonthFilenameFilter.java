package com.example.coursework.helpers;

import lombok.Data;

import java.io.File;
import java.io.FilenameFilter;

@Data
public class MonthFilenameFilter implements FilenameFilter {
    private String targetMonth;

    public MonthFilenameFilter(final String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public boolean accept(final File dir, final String name) {
        String[] parts = name.split("-");
        return parts.length > 2 && parts[2].equals(targetMonth);
    }
}
