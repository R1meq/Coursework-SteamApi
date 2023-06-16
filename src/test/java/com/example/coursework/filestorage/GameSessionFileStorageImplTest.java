package com.example.coursework.filestorage;

import com.example.coursework.filestorage.ipml.GameSessionFileStorageImpl;
import com.example.coursework.models.GameSession;
import com.example.coursework.models.enams.Status;
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
public class GameSessionFileStorageImplTest {
    private static final String DIRNAME = "src/main/resources/gameSessions/result_files";
    private static final String EXPECTED_PATH_WRITE = "src/main/resources/gameSessions/expected_files/write.csv";
    private static final String EXPECTED_PATH_UPDATE = "src/main/resources/gameSessions/expected_files/update.csv";
    private static final String EXPECTED_PATH_DELETE  = "src/main/resources/gameSessions/expected_files/delete.csv";
    private static final String FILE_PATH_RESULT = "src/main/resources/gameSessions/result_files/result-" + getCurrentDate() + ".csv";

    GameSessionFileStorageImpl gameSessionFileStorage;

    @BeforeEach
    public void setUp() {
        gameSessionFileStorage = new GameSessionFileStorageImpl();
    }

    @Test
    @Order(1)
    public void writeToCsvTest() throws IOException {
        GameSession gameSession = new GameSession(1, Status.ONLINE, "22:30", " ", 1, 2);
        gameSessionFileStorage.writeToFile(gameSession, FILE_PATH_RESULT);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_WRITE).toPath();
        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(2)
    public void updateToCsvTest() throws IOException {
        GameSession gameSession = new GameSession(1, Status.OFFLINE, "22:30", "23:30", 1, 2);
        gameSessionFileStorage.updateFromFile(1, gameSession, DIRNAME);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_UPDATE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(3)
    public void deleteToCsvTest() throws IOException {
        gameSessionFileStorage.deleteFromFile(1, DIRNAME);

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
