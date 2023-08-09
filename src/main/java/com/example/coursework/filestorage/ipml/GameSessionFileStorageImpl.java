package com.example.coursework.filestorage.ipml;

import com.example.coursework.filestorage.GameSessionFileStorage;
import com.example.coursework.models.GameSession;

import com.example.coursework.models.enams.Status;
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
public class GameSessionFileStorageImpl implements GameSessionFileStorage {
    public static final String DIRNAME = "src/main/resources/gameSessions/";
    public static final String FILE_PATH = "src/main/resources/gameSessions/gameSession-" + getCurrentDate() + ".csv";
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Integer, GameSession> hashMap = new HashMap<>();

    @Override
    public Map<Integer, GameSession> getHashMap() {
        return hashMap;
    }

    @Override
    @PostConstruct
    public void saveInHashMap() {
        List<GameSession> gameSessions = readAllFiles(DIRNAME);

        if (gameSessions != null) {
            for (GameSession gameSession : gameSessions) {
                hashMap.put(gameSession.getSessionId(), gameSession);

                idCounter.set(getLastId(DIRNAME));
            }
        }
    }

    @Override
    public void updateFromFile(final Integer id, final GameSession entity, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<GameSession> list = fileReader(tempFile);
        list.removeIf((n) -> n.getSessionId().equals(id));
        list.add(entity);
        list.sort(Comparator.comparing(GameSession::getSessionId));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(GameSession.getHeaders() + System.lineSeparator());
            for (GameSession gameSession : list) {
                fileWriter.write(gameSession.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromFile(final Integer id, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<GameSession> list = fileReader(tempFile);
        list.removeIf((n) -> n.getSessionId().equals(id));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(GameSession.getHeaders() + System.lineSeparator());
                for (GameSession gameSession : list) {
                    fileWriter.write(gameSession.toCsv() + System.lineSeparator());
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(final GameSession entity, final String filePath) {
        File file = new File(filePath);
        boolean fileExists = file.exists();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!fileExists) {
                fileWriter.write(GameSession.getHeaders() + System.lineSeparator());
            }
            fileWriter.write(entity.toCsv() + System.lineSeparator());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameSession> fileReader(final File file) {
        List<GameSession> gameSessions = new LinkedList<>();
        boolean skipHeaders = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                    GameSession gameSession = new GameSession();

                    String[] values = line.split(", ");

                    gameSession.setSessionId(Integer.parseInt(values[0]));
                    gameSession.setStatusOfGame(Status.valueOf(values[1]));
                    gameSession.setStartGame(String.valueOf((values[2])));
                    gameSession.setEndGame(String.valueOf((values[3])));

                    if (Objects.equals(values[4], "null")) {
                        gameSession.setUserId(null);
                    } else {
                        gameSession.setUserId(Integer.parseInt((values[4])));
                    }

                    if (Objects.equals(values[5], "null")) {
                        gameSession.setGameId(null);
                    } else {
                        gameSession.setGameId(Integer.parseInt((values[5])));
                    }

                    gameSessions.add(gameSession);
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameSessions;
    }

    @Override
    public List<GameSession> readAllFiles(final String dirname) {
        List<GameSession> gameSessionList = new ArrayList<>();
        File[] files = getCatalog(dirname);

        assert files != null;
        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                File file1 = new File(file.toURI());
                gameSessionList.addAll(fileReader(file1));
            }
        }

        return gameSessionList;
    }

    @Override
    public Integer incrementIdCounter() {
        return idCounter.incrementAndGet();
    }

    @Override
    public List<GameSession> getList() {
        return new ArrayList<>(hashMap.values());
    }
}
