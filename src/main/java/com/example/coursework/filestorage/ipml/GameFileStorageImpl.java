package com.example.coursework.filestorage.ipml;

import com.example.coursework.exceptions.NameAlreadyUsedException;
import com.example.coursework.filestorage.GameFileStorage;
import com.example.coursework.models.Game;
import com.example.coursework.models.enams.Games;
import com.example.coursework.models.enams.GenreOfGame;
import com.example.coursework.models.enams.Review;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.coursework.helpers.FileStorageHelper.findFile;
import static com.example.coursework.helpers.FileStorageHelper.getCatalog;
import static com.example.coursework.helpers.FileStorageHelper.getLastId;
import static com.example.coursework.helpers.LocalDateGetter.getCurrentDate;

@Component
public class GameFileStorageImpl implements GameFileStorage {
    public static final String DIRNAME = "src/main/resources/games";
    public static final String FILE_PATH = "src/main/resources/games/game-" + getCurrentDate() + ".csv";
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Integer, Game> hashMap = new HashMap<>();
    private final HashSet<Games> uniqueNameOfGame = new HashSet<>();

    public void checkNameOfGame(final Integer id, final Game game) {
        if (id != null) {
            Game oldGame = hashMap.get(id);
            if (oldGame != null && !oldGame.getNameOfGame().equals(game.getNameOfGame())) {
                if (uniqueNameOfGame.contains(game.getNameOfGame())) {
                    throw new NameAlreadyUsedException("nickname is already used");
                } else {
                    uniqueNameOfGame.remove(oldGame.getNameOfGame());
                    uniqueNameOfGame.add(game.getNameOfGame());
                    game.setNameOfGame(game.getNameOfGame());
                }
            }
        } else {
            if (uniqueNameOfGame.contains(game.getNameOfGame())) {
                throw new NameAlreadyUsedException("name of Game already used");
            } else {
                uniqueNameOfGame.add(game.getNameOfGame());
                game.setNameOfGame(game.getNameOfGame());
            }
        }
    }


    public void deleteFromHashSet(final Integer id) {
        Games oldValue = hashMap.get(id).getNameOfGame();
        uniqueNameOfGame.remove(oldValue);
    }

    @Override
    @PostConstruct
    public void saveInHashMap() {
        List<Game> games = readAllFiles(DIRNAME);

        if (games != null) {
            for (Game game : games) {
                hashMap.put(game.getGameId(), game);
                uniqueNameOfGame.add(game.getNameOfGame());
                idCounter.set(getLastId(DIRNAME));
            }
        }
    }

    @Override
    public void updateFromFile(final Integer id, final Game updateGame, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<Game> list = fileReader(tempFile);
        list.removeIf((n) -> n.getGameId().equals(id));
        list.add(updateGame);
        list.sort(Comparator.comparing(Game::getGameId));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(Game.getHeaders() + System.lineSeparator());
            for (Game game : list) {
                fileWriter.write(game.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromFile(final Integer id, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<Game> list = fileReader(tempFile);
        list.removeIf((n) -> n.getGameId().equals(id));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(Game.getHeaders() + System.lineSeparator());
            for (Game game : list) {
                fileWriter.write(game.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(final Game game, final String filePath) {
        File file = new File(filePath);
        boolean fileExists = file.exists();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!fileExists) {
                fileWriter.write(Game.getHeaders() + System.lineSeparator());
            }
            fileWriter.write(game.toCsv() + System.lineSeparator());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> fileReader(final File file) {
        List<Game> games = new LinkedList<>();
        boolean skipHeaders = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                    Game game = new Game();

                    String[] values = line.split(", ");

                    game.setGameId(Integer.parseInt(values[0]));
                    game.setNameOfGame(Games.valueOf(values[1]));
                    game.setDescription(values[2]);
                    game.setReviews(Review.valueOf(values[3]));
                    game.setGenreOfGame(GenreOfGame.valueOf(values[4]));

                    if (Objects.equals(values[5], "null")) {
                        game.setPublisherId(null);
                    } else {
                        game.setPublisherId(Integer.parseInt(values[5]));
                    }

                    List<Integer> integerList = new ArrayList<>();
                    for (int i = 6; i < values.length; i++) {
                        String value = values[i].replaceAll("[\\[\\]]", "");
                        if (!value.isEmpty()) {
                            integerList.add(Integer.parseInt(value));
                        }
                    }
                    game.setUsersId(integerList);

                    games.add(game);
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public List<Game> readAllFiles(final String dirname) {
        List<Game> gameList = new ArrayList<>();
        File[] files = getCatalog(dirname);

        assert files != null;
        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                File file1 = new File(file.toURI());
                gameList.addAll(fileReader(file1));
            }
        }

        return gameList;
    }

    @Override
    public Integer incrementIdCounter() {
        return idCounter.incrementAndGet();
    }

    @Override
    public Map<Integer, Game> getHashMap() {
        return hashMap;
    }

    @Override
    public List<Game> getList() {
        return new ArrayList<>(getHashMap().values());
    }
}
