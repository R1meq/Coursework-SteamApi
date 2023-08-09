package com.example.coursework.filestorage;

import com.example.coursework.filestorage.ipml.BrwsrReqFileStorageImpl;
import com.example.coursework.helpers.LocalDateGetter;
import com.example.coursework.models.BrwsrReq;
import com.example.coursework.models.enams.SupportedBrwsr;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.coursework.helpers.FileStorageHelper.getCatalog;
import static com.example.coursework.helpers.LocalDateGetter.getCurrentDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrwsrReqFileStorageImplTest {
    private static final String DIRNAME = "src/main/resources/brwsrReqs/result_files";
    private static final String EXPECTED_PATH_WRITE = "src/main/resources/brwsrReqs/expected_files/write.csv";
    private static final String EXPECTED_PATH_UPDATE = "src/main/resources/brwsrReqs/expected_files/update.csv";
    private static final String EXPECTED_PATH_DELETE  = "src/main/resources/brwsrReqs/expected_files/delete.csv";
    private static final String FILE_PATH_RESULT = "src/main/resources/brwsrReqs/result_files/result-" + getCurrentDate() + ".csv";

    BrwsrReqFileStorageImpl brwsrReqFileStorage;


    @BeforeEach
    void setUp() {
        brwsrReqFileStorage = new BrwsrReqFileStorageImpl();
    }

    @Test
    @Order(1)
    public void writeToCsvTest() throws IOException {
        BrwsrReq brwsrReq1 = new BrwsrReq(1, SupportedBrwsr.APPLE_SAFARI_VERSION_8);
        brwsrReqFileStorage.writeToFile(brwsrReq1, FILE_PATH_RESULT);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_WRITE).toPath();
        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(2)
    public void updateToCsvTest() throws IOException {
        BrwsrReq brwsrReq1 = new BrwsrReq(1, SupportedBrwsr.MOZILLA_FIREFOX_VERSION_42);
        brwsrReqFileStorage.updateFromFile(1, brwsrReq1, DIRNAME);


        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_UPDATE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(3)
    public void deleteToCsvTest() throws IOException {
        brwsrReqFileStorage.deleteFromFile(1, DIRNAME);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_DELETE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

   @AfterAll
   public static void tearDownAll() {
        File[] files = getCatalog(DIRNAME);

        for (File file : files) {
            file.delete();
        }
   }
}
