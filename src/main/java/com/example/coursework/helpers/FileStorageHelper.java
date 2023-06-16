package com.example.coursework.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class FileStorageHelper {

    public static File[] getCatalog(final String dirname) {
        File f1 = new File(dirname);
        return f1.listFiles(new MonthFilenameFilter("06"));
    }

    public static Integer getLastId(final String dirname) {
        Integer maxId = null;
        File[] files = getCatalog(dirname);

        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                Integer lastId = findLastId(file);
                if (lastId != null) {
                    maxId = lastId;
                }
            }
        }
        return maxId != null ? maxId : 0;
    }

    public static Integer findLastId(final File file) {
        List<Integer> integers  = new LinkedList<>();
        boolean skipHeaders = true;

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                    String[] values = line.split(", ");
                    if (values[0] != null) {
                        integers.add(Integer.parseInt(values[0]));
                    }
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return integers.isEmpty() ? null : integers.stream().max(Comparator.comparing(i -> i)).get();
    }

    public static String findFile(final Integer id, final String dirname) {
        String filePath = null;
        File[] files = getCatalog(dirname);

        for (File file : files) {
            boolean skipHeaders = true;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!skipHeaders) {
                        String[] values = line.split(", ");
                        if (Integer.parseInt(values[0]) == id) {
                            filePath = file.getPath();
                            break;
                        }
                    } else {
                        skipHeaders = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }
}
