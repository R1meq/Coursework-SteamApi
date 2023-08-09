package com.example.coursework.services.impl;

import com.example.coursework.filestorage.ipml.GameFileStorageImpl;
import com.example.coursework.filestorage.ipml.UserFileStorageImpl;
import com.example.coursework.models.Game;
import com.example.coursework.models.GameSession;
import com.example.coursework.models.Publisher;
import com.example.coursework.models.User;
import com.example.coursework.filestorage.ipml.GameSessionFileStorageImpl;
import com.example.coursework.filestorage.ipml.PublisherFileStorageImpl;
import com.example.coursework.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final UserFileStorageImpl userFileStorageImpl;

    private final GameFileStorageImpl gameFileStorageImpl;

    private final PublisherFileStorageImpl publisherFileStorageImpl;

    private final GameSessionFileStorageImpl gameSessionFileStorageImpl;

    @Autowired
    public GameServiceImpl(final UserFileStorageImpl userFileStorageImpl,
                           final GameFileStorageImpl gameFileStorageImpl,
                           final PublisherFileStorageImpl publisherFileStorageImpl,
                           final GameSessionFileStorageImpl gameSessionFileStorageImpl) {
        this.userFileStorageImpl = userFileStorageImpl;
        this.gameFileStorageImpl = gameFileStorageImpl;
        this.publisherFileStorageImpl = publisherFileStorageImpl;
        this.gameSessionFileStorageImpl = gameSessionFileStorageImpl;
    }

    @Override
    public List<Game> getAll() {
        return new LinkedList<>(gameFileStorageImpl.getHashMap().values());
    }

    @Override
    public Game getById(final Integer id) {
        return gameFileStorageImpl.getHashMap().get(id);
    }

    @Override
    public Game create(final Game entity) {
        gameFileStorageImpl.checkNameOfGame(null, entity);

        entity.setPublisherId(publisherId(entity));
        entity.setUsersId(usersId(entity));
        Integer id = gameFileStorageImpl.incrementIdCounter();
        entity.setGameId(id);
        gameFileStorageImpl.getHashMap().put(id, entity);
        gameFileStorageImpl.writeToFile(entity, GameFileStorageImpl.FILE_PATH);
        return entity;
    }

    @Override
    public Game update(final Integer id, final Game entity) {
        gameFileStorageImpl.checkNameOfGame(id, entity);

        entity.setPublisherId(publisherId(entity));
        entity.setUsersId(usersId(entity));
        entity.setGameId(id);
        gameFileStorageImpl.getHashMap().replace(id, entity);
        gameFileStorageImpl.updateFromFile(id, entity, GameFileStorageImpl.DIRNAME);
        return entity;
    }

    @Override
    public void deleteById(final Integer id) {
        List<GameSession> list = gameSessionFileStorageImpl.getList();

        for (GameSession gameSession : list) {
            if (gameSession.getGameId() != null && gameSession.getGameId().equals(id)) {
                gameSessionFileStorageImpl.getHashMap().remove(gameSession.getSessionId());
                gameSessionFileStorageImpl.deleteFromFile(gameSession.getSessionId(), GameSessionFileStorageImpl.DIRNAME);
            }
        }
        gameFileStorageImpl.deleteFromHashSet(id);
        gameFileStorageImpl.getHashMap().remove(id);
        gameFileStorageImpl.deleteFromFile(id, GameFileStorageImpl.DIRNAME);
    }


    public List<Integer> usersId(Game game) {
        List<User> userList = userFileStorageImpl.getList();
        List<Integer> usersId = new ArrayList<>(game.getUsersId());
        List<Integer> result = new ArrayList<>();

        for (User user : userList) {
            if (usersId.contains(user.getUserId())) {
                result.add(user.getUserId());
            }
        }
        return result;
    }

    public Integer publisherId(Game game) {
        List<Publisher> publishers = publisherFileStorageImpl.getList();
        Integer id = null;

        for (Publisher publisher : publishers) {
            if (publisher.getPublisherId().equals(game.getPublisherId())) {
                id = game.getPublisherId();
                break;
            }
        }
        return id;
    }
}
