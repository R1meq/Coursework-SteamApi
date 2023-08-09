package com.example.coursework.controllers.impl;

import com.example.coursework.controllers.GameSessionController;
import com.example.coursework.models.GameSession;
import com.example.coursework.services.impl.GameSessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/gameSessions")
public class GameSessionControllerImpl implements GameSessionController {

    private final GameSessionServiceImpl gameSessionServiceImpl;

    @Autowired
    public GameSessionControllerImpl(final GameSessionServiceImpl gameSessionServiceImpl) {
        this.gameSessionServiceImpl = gameSessionServiceImpl;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameSession> getAll() {
        return gameSessionServiceImpl.getAll();
    }

    @Override
    @GetMapping(path = "/{Id}")
    public ResponseEntity<GameSession> getById(final @PathVariable("Id") Integer id) {
        if (gameSessionServiceImpl.getById(id) != null) {
            return ResponseEntity.ok(gameSessionServiceImpl.getById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GameSession create(final @RequestBody GameSession entity) {
        return gameSessionServiceImpl.create(entity);
    }

    @Override
    @PutMapping("/{Id}")
    public ResponseEntity<GameSession> update(final @PathVariable("Id") Integer id,
                                              final @RequestBody GameSession entity) {
        if (gameSessionServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gameSessionServiceImpl.update(id, entity));
    }

    @Override
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<GameSession> deleteById(final @PathVariable("Id") Integer id) {
        if (gameSessionServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        gameSessionServiceImpl.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/startGame/{Id}")
    public ResponseEntity<GameSession> startGame(final @PathVariable("Id") Integer id) {
        if (gameSessionServiceImpl.getById(id) != null) {
            return ResponseEntity.ok(gameSessionServiceImpl.startGame(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/endGame/{Id}")
    public ResponseEntity<GameSession> endGame(final @PathVariable("Id") Integer id) {
        if (gameSessionServiceImpl.getById(id) != null) {
            return ResponseEntity.ok(gameSessionServiceImpl.endGame(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
