package com.example.coursework.services.impl;

import com.example.coursework.filestorage.ipml.GameFileStorageImpl;
import com.example.coursework.filestorage.ipml.GameSessionFileStorageImpl;
import com.example.coursework.filestorage.ipml.UserFileStorageImpl;
import com.example.coursework.models.Game;
import com.example.coursework.models.GameSession;
import com.example.coursework.models.User;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserFileStorageImpl userFileStorageImpl;

    private final GameFileStorageImpl gameFileStorageImpl;

    private final GameSessionFileStorageImpl gameSessionFileStorageImpl;

    @Autowired
    public UserServiceImpl(final UserFileStorageImpl userFileStorageImpl,
                           final GameFileStorageImpl gameFileStorageImpl,
                           final GameSessionFileStorageImpl gameSessionFileStorageImpl) {
        this.userFileStorageImpl = userFileStorageImpl;
        this.gameFileStorageImpl = gameFileStorageImpl;
        this.gameSessionFileStorageImpl = gameSessionFileStorageImpl;
    }

    @Override
    public List<User> getAll() {
        return userFileStorageImpl.getList();
    }

    @Override
    public User getById(final Integer id) {
        return userFileStorageImpl.getHashMap().get(id);
    }

    @Override
    public User create(final User entity) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfRegister = LocalDate.parse(entity.getDateOfRegister());
        if (dateOfRegister.isBefore(currentDate)) {
            throw new IllegalArgumentException("the date of registration cannot be a past date");
        }

        userFileStorageImpl.checkNicknames(null, entity);

        Integer id = userFileStorageImpl.incrementIdCounter();
        entity.setUserId(id);
        userFileStorageImpl.getHashMap().put(id, entity);
        userFileStorageImpl.writeToFile(entity, UserFileStorageImpl.FILE_PATH);
        return entity;
    }

    @Override
    public User update(final Integer id, final User entity) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfRegister = LocalDate.parse(entity.getDateOfRegister());
        if (dateOfRegister.isBefore(currentDate)) {
            throw new IllegalArgumentException("the date of registration cannot be a past date");
        }

        userFileStorageImpl.checkNicknames(id, entity);
        entity.setUserId(id);
        userFileStorageImpl.getHashMap().replace(id, entity);
        userFileStorageImpl.updateFromFile(id, entity, UserFileStorageImpl.DIRNAME);
        return entity;
    }

    @Override
    public void deleteById(final Integer id) {
        List<Game> gameList = gameFileStorageImpl.getList();
        List<GameSession> gameSessionList = gameSessionFileStorageImpl.getList();


         for (GameSession gameSession : gameSessionList) {
             if (gameSession.getUserId() != null && gameSession.getUserId().equals(id)) {
                gameSessionFileStorageImpl.getHashMap().remove(gameSession.getSessionId());
                gameSessionFileStorageImpl.deleteFromFile(gameSession.getSessionId(), GameSessionFileStorageImpl.DIRNAME);
             }
         }

         for (Game game : gameList) {
             if (game.getUsersId().contains(id)) {
                game.getUsersId().remove(id);
                gameFileStorageImpl.getHashMap().replace(game.getGameId(), game);
                gameFileStorageImpl.updateFromFile(game.getGameId(), game, GameFileStorageImpl.DIRNAME);
             }
         }

        userFileStorageImpl.deleteFromHashSet(id);
        userFileStorageImpl.getHashMap().remove(id);
        userFileStorageImpl.deleteFromFile(id, UserFileStorageImpl.DIRNAME);
    }
}
