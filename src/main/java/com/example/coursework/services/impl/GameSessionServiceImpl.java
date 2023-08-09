package com.example.coursework.services.impl;

import com.example.coursework.filestorage.ipml.GameFileStorageImpl;
import com.example.coursework.filestorage.ipml.GameSessionFileStorageImpl;
import com.example.coursework.filestorage.ipml.UserFileStorageImpl;
import com.example.coursework.models.Game;
import com.example.coursework.models.GameSession;
import com.example.coursework.models.User;
import com.example.coursework.models.enams.Status;
import com.example.coursework.services.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionFileStorageImpl gameSessionFileStorageImpl;

    private final GameFileStorageImpl gameFileStorageImpl;

    private  final UserFileStorageImpl userFileStorageImpl;

    @Autowired
    public GameSessionServiceImpl(final GameSessionFileStorageImpl gameSessionFileStorageImpl,
                                  final GameFileStorageImpl gameFileStorageImpl,
                                  final UserFileStorageImpl userFileStorageImpl) {
        this.gameSessionFileStorageImpl = gameSessionFileStorageImpl;
        this.gameFileStorageImpl = gameFileStorageImpl;
        this.userFileStorageImpl = userFileStorageImpl;
    }


    @Override
    public List<GameSession> getAll() {
        return gameSessionFileStorageImpl.getList();
    }

    @Override
    public GameSession getById(final Integer id) {
        return gameSessionFileStorageImpl.getHashMap().get(id);
    }

    @Override
    public GameSession create(final GameSession entity) {
        entity.setUserId(userId(entity));
        entity.setGameId(gameId(entity));

        Integer id = gameSessionFileStorageImpl.incrementIdCounter();
        entity.setSessionId(id);
        gameSessionFileStorageImpl.getHashMap().put(id, entity);
        gameSessionFileStorageImpl.writeToFile(entity, GameSessionFileStorageImpl.FILE_PATH);
        return entity;
    }

    @Override
    public GameSession update(final Integer id, final GameSession entity) {
        entity.setUserId(userId(entity));
        entity.setGameId(gameId(entity));

        entity.setSessionId(id);
        gameSessionFileStorageImpl.getHashMap().replace(id, entity);
        gameSessionFileStorageImpl.updateFromFile(id, entity, GameSessionFileStorageImpl.DIRNAME);
        return entity;
    }

    @Override
    public void deleteById(final Integer id) {
        gameSessionFileStorageImpl.getHashMap().remove(id);
        gameSessionFileStorageImpl.deleteFromFile(id, GameSessionFileStorageImpl.DIRNAME);
    }

    public Integer gameId(final GameSession gameSession) {
        List<Game> gameList = gameFileStorageImpl.getList();
        Integer id = null;

        for (Game game : gameList) {
            if (gameSession.getGameId().equals(game.getGameId())
                    && game.getUsersId().contains(gameSession.getUserId())) {
                id = gameSession.getGameId();
                break;
            }
        }
        return id;
    }

    public Integer userId(final GameSession gameSession) {
        List<User> usersList = userFileStorageImpl.getList();
        Integer id = null;

        for (User user : usersList) {
            if (gameSession.getUserId().equals(user.getUserId())) {
                id = gameSession.getUserId();
                break;
            }
        }
        return id;
    }

    public GameSession endGame(final Integer id) {
        List<GameSession> list = gameSessionFileStorageImpl.getList();
        for (GameSession gameSession : list) {
            if (Objects.equals(gameSession.getStartGame(), "")) {
                throw new RuntimeException("you cannot finish the game if it has not been started");
            }
            if (gameSession.getSessionId().equals(id)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime = LocalDateTime.now().format(formatter);
                gameSession.setStatusOfGame(Status.OFFLINE);
                gameSession.setEndGame(currentTime);
                gameSessionFileStorageImpl.updateFromFile(id, gameSession, GameSessionFileStorageImpl.DIRNAME);
                return gameSession;
            }
        }
        return null;
    }

    public GameSession startGame(final Integer id) {
        List<GameSession> list = gameSessionFileStorageImpl.getList();
        for (GameSession gameSession : list) {
            if (gameSession.getSessionId().equals(id)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime = LocalDateTime.now().format(formatter);
                gameSession.setStatusOfGame(Status.ONLINE);
                gameSession.setStartGame(currentTime);
                gameSessionFileStorageImpl.updateFromFile(id, gameSession, GameSessionFileStorageImpl.DIRNAME);
                return gameSession;
            }
        }
        return null;
    }
}
