package com.example.coursework.filestorage;

import com.example.coursework.filestorage.ipml.PublisherFileStorageImpl;
import com.example.coursework.models.Publisher;
import com.example.coursework.models.enams.Publishers;
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
public class PublisherFileStorageImplTest {
    private static final String DIRNAME = "src/main/resources/publishers/result_files";
    private static final String EXPECTED_PATH_WRITE = "src/main/resources/publishers/expected_files/write.csv";
    private static final String EXPECTED_PATH_UPDATE = "src/main/resources/publishers/expected_files/update.csv";
    private static final String EXPECTED_PATH_DELETE  = "src/main/resources/publishers/expected_files/delete.csv";
    private static final String FILE_PATH_RESULT = "src/main/resources/publishers/result_files/result-" + getCurrentDate() + ".csv";

    PublisherFileStorageImpl publisherFileStorage;

    @BeforeEach
    public void setUp() {
        publisherFileStorage = new PublisherFileStorageImpl();
    }

    @Test
    @Order(1)
    public void writeToCsvTest() throws IOException {
        Publisher publisher = new Publisher(1, Publishers.UBISOFT);
        publisherFileStorage.writeToFile(publisher, FILE_PATH_RESULT);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_WRITE).toPath();
        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(2)
    public void updateToCsvTest() throws IOException {
        Publisher publisher1 = new Publisher(1, Publishers.ACTIVISION);
        publisherFileStorage.updateFromFile(1, publisher1, DIRNAME);


        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_UPDATE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(3)
    public void deleteToCsvTest() throws IOException {
        publisherFileStorage.deleteFromFile(1, DIRNAME);

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
