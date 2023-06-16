package com.example.coursework.services.impl;

import com.example.coursework.exceptions.PublisherUpdateNotAllowedException;
import com.example.coursework.filestorage.ipml.GameFileStorageImpl;
import com.example.coursework.filestorage.ipml.GameSessionFileStorageImpl;
import com.example.coursework.models.Game;
import com.example.coursework.models.GameSession;
import com.example.coursework.models.Publisher;
import com.example.coursework.services.PublisherService;
import com.example.coursework.filestorage.ipml.PublisherFileStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherFileStorageImpl publisherFileStorageImpl;

    private final GameFileStorageImpl gameFileStorageImpl;

    private final GameSessionFileStorageImpl gameSessionFileStorage;

    @Autowired
    public PublisherServiceImpl(final PublisherFileStorageImpl publisherFileStorageImpl,
                                final GameFileStorageImpl gameFileStorageImpl,
                                final GameSessionFileStorageImpl gameSessionFileStorage) {
        this.publisherFileStorageImpl = publisherFileStorageImpl;
        this.gameFileStorageImpl = gameFileStorageImpl;
        this.gameSessionFileStorage = gameSessionFileStorage;
    }

    @Override
    public List<Publisher> getAll() {
        return publisherFileStorageImpl.getList();
    }

    @Override
    public Publisher getById(final Integer id) {
        return publisherFileStorageImpl.getHashMap().get(id);
    }

    @Override
    public Publisher create(final Publisher entity) {
        publisherFileStorageImpl.checkNameOfPublisher(entity);
        Integer id = publisherFileStorageImpl.incrementIdCounter();
        entity.setPublisherId(id);
        publisherFileStorageImpl.getHashMap().put(id, entity);
        publisherFileStorageImpl.writeToFile(entity, PublisherFileStorageImpl.FILE_PATH);
        return entity;
    }

    @Override
    public Publisher update(final Integer id, final Publisher entity) {
        List<Game> games = gameFileStorageImpl.getList();

        for (Game game :games) {
            if(game.getPublisherId().equals(id)) {
                throw new PublisherUpdateNotAllowedException("you can't update publisher what already have at least one game");
            }
        }

        publisherFileStorageImpl.checkNameOfPublisher(entity);
        publisherFileStorageImpl.deleteFromHashSet(id);
        entity.setPublisherId(id);
        publisherFileStorageImpl.getHashMap().replace(id, entity);
        publisherFileStorageImpl.updateFromFile(id, entity, PublisherFileStorageImpl.DIRNAME);
        return entity;
    }

    @Override
    public void deleteById(final Integer id) {
        List<Game> gameList = gameFileStorageImpl.getList();
        List<GameSession> gameSessionList = gameSessionFileStorage.getList();

        for (Game game : gameList) {
            for (GameSession gameSession : gameSessionList) {
                if (game.getPublisherId() != null && game.getPublisherId().equals(id)) {
                    gameFileStorageImpl.getHashMap().remove(game.getGameId());
                    gameFileStorageImpl.deleteFromFile(game.getGameId(), GameFileStorageImpl.DIRNAME);
                    if (gameSession.getGameId() != null && gameSession.getGameId().equals(game.getGameId())) {
                        gameSessionFileStorage.getHashMap().remove(gameSession.getSessionId());
                        gameSessionFileStorage.deleteFromFile(gameSession.getSessionId(), GameSessionFileStorageImpl.DIRNAME);
                    }
                }
            }
        }

        publisherFileStorageImpl.deleteFromHashSet(id);
        publisherFileStorageImpl.getHashMap().remove(id);
        publisherFileStorageImpl.deleteFromFile(id, PublisherFileStorageImpl.DIRNAME);

    }
}
