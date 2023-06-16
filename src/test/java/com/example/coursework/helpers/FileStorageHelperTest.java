package com.example.coursework.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;


import static com.example.coursework.helpers.FileStorageHelper.findFile;
import static com.example.coursework.helpers.FileStorageHelper.findLastId;
import static com.example.coursework.helpers.FileStorageHelper.getCatalog;
import static com.example.coursework.helpers.FileStorageHelper.getLastId;
import static com.example.coursework.helpers.LocalDateGetter.getCurrentDate;

public class FileStorageHelperTest {
    private static final String DIRNAME = "src/main/resources/filesTest";
    private static final String FILE_PATH = "src/main/resources/filesTest/game-2023-06-13.csv";
    private static final String WRONG_PATH = "src/main/resources/filesTest/dontExistFile.csv";
    private static final String WRONG_DIRNAME = "src/main/resources/dontExistCatalog";


    @Test
    public void getCatalogTest() {
        File[] result = getCatalog(DIRNAME);

        Assertions.assertEquals(2, result.length);
        Assertions.assertNotEquals("game-2023-07-13.csv", result[0].getName());
    }

    @Test
    public void findLastIdTest() {
        Integer result = findLastId(new File(FILE_PATH));

        Assertions.assertEquals(2, result);
    }

    @Test
    public void findFileIdDontExist() {
        String expectedFilePath = null;
        String resultFilePath = findFile(999, DIRNAME);

        Assertions.assertEquals(expectedFilePath, resultFilePath);
    }

    @Test
    public void findLastIdOfDontExistFile() {
        Integer expectedLastId = null;
        Integer resultLastId = findLastId(new File(WRONG_PATH));

        Assertions.assertEquals(expectedLastId, resultLastId);
    }

    @Test
    public void getLastIdOfDontExistCatalog() {
        Integer expectedLastId = null;
        Integer resultLastId = getLastId(WRONG_DIRNAME);

        Assertions.assertEquals(expectedLastId, resultLastId);
    }

    @Test
    public void getLastIdTest() {
        Integer result = getLastId(DIRNAME);

        Assertions.assertEquals(6, result);
        Assertions.assertNotEquals(2, result);
    }

    @Test
    public void findFileTest() {
        String result = findFile(3, DIRNAME);
        String expected = "src\\main\\resources\\filesTest\\game-2023-06-20.csv";
        Assertions.assertEquals(expected, result);
    }
}
