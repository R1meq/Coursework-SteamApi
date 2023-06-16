package com.example.coursework.filestorage;

import com.example.coursework.filestorage.ipml.GameFileStorageImpl;
import com.example.coursework.models.Game;
import com.example.coursework.models.enams.Games;
import com.example.coursework.models.enams.GenreOfGame;
import com.example.coursework.models.enams.Review;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.coursework.helpers.FileStorageHelper.getCatalog;
import static com.example.coursework.helpers.LocalDateGetter.getCurrentDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameFileStorageImplTest {
    private static final String DIRNAME = "src/main/resources/games/result_files";
    private static final String EXPECTED_PATH_WRITE = "src/main/resources/games/expected_files/write.csv";
    private static final String EXPECTED_PATH_UPDATE = "src/main/resources/games/expected_files/update.csv";
    private static final String EXPECTED_PATH_DELETE  = "src/main/resources/games/expected_files/delete.csv";
    private static final String FILE_PATH_RESULT = "src/main/resources/games/result_files/result-" + getCurrentDate() + ".csv";

    GameFileStorageImpl gameFileStorage;

    @BeforeEach
    public void setUp() {
        gameFileStorage = new GameFileStorageImpl();
    }


    @Test
    @Order(1)
    public void writeToCsvTest() throws IOException {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        Game game = new Game(1, Games.ASSASSINS_CREED_UNITY,
                "stealth game played from a third-person",
                Review.POSITIVE, GenreOfGame.OPEN_WORLD, 2, integerList);

        gameFileStorage.writeToFile(game, FILE_PATH_RESULT);

        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_WRITE).toPath();
        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(2)
    public void updateToCsvTest() throws IOException {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        Game game = new Game(1, Games.ASSASSINS_CREED_UNITY,
                "stealth game played from a third-person",
                Review.NEGATIVE, GenreOfGame.OPEN_WORLD, 2, integerList);

        gameFileStorage.updateFromFile(1, game, DIRNAME);


        Path result = new File(FILE_PATH_RESULT).toPath();
        Path expected = new File(EXPECTED_PATH_UPDATE).toPath();

        Assertions.assertEquals(-1L, Files.mismatch(expected, result));
    }

    @Test
    @Order(3)
    public void deleteToCsvTest() throws IOException {
        gameFileStorage.deleteFromFile(1, DIRNAME);

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
