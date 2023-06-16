package com.example.coursework.filestorage;

import com.example.coursework.filestorage.ipml.UserFileStorageImpl;
import com.example.coursework.models.User;
import com.example.coursework.models.enams.Countries;
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
public class UserFileStorageImplTest {
    private static final String DIRNAME = "src/main/resources/users/result_files";
    private static final String EXPECTED_PATH_WRITE = "src/main/resources/users/expected_files/write.csv";
    private static final String EXPECTED_PATH_UPDATE = "src/main/resources/users/expected_files/update.csv";
    private static final String EXPECTED_PATH_DELETE  = "src/main/resources/users/expected_files/delete.csv";
    private static final String FILE_PATH_RESULT = "src/main/resources/users/result_files/result-" + getCurrentDate() + ".csv";

    UserFileStorageImpl userFileStorage;

    @BeforeEach
    public void setUp() {
        userFileStorage = new UserFileStorageImpl();
    }

    @Test
    @Order(1)
    public void writeToCsvTest() throws IOException {
        User user = new User(1, "R1meq", "2023-06-20", Countries.POLAND, "Roman", "Senyk");
        userFileStorage.writeToFile(user, FILE_PATH_RESULT);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_WRITE).toPath();
        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(2)
    public void updateToCsvTest() throws IOException {
        User user = new User(1, "R1meq", "2023-06-20", Countries.FRANCE, "Roman", "Senyk");
        userFileStorage.updateFromFile(1, user, DIRNAME);


        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_UPDATE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(3)
    public void deleteToCsvTest() throws IOException {
        userFileStorage.deleteFromFile(1, DIRNAME);

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
