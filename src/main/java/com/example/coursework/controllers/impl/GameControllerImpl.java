package com.example.coursework.controllers.impl;

import com.example.coursework.models.Game;
import com.example.coursework.services.impl.GameServiceImpl;
import com.example.coursework.controllers.GameController;
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
@RequestMapping("/games")
public class GameControllerImpl implements GameController {

    private final GameServiceImpl gameServiceImpl;

    @Autowired
    public GameControllerImpl(final GameServiceImpl gameServiceImpl) {
        this.gameServiceImpl = gameServiceImpl;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getAll() {
        return gameServiceImpl.getAll();
    }

    @Override
    @GetMapping(path = "/{Id}")
    public ResponseEntity<Game> getById(final @PathVariable("Id") Integer id) {
       if (gameServiceImpl.getById(id) != null) {
           return ResponseEntity.ok(gameServiceImpl.getById(id));
       } else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }

    @Override
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(final @RequestBody Game entity) {
        return gameServiceImpl.create(entity);
    }

    @Override
    @PutMapping("/{Id}")
    public ResponseEntity<Game> update(final @PathVariable("Id") Integer id,
                                       final @RequestBody Game game) {
        if (gameServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gameServiceImpl.update(id, game));
    }

    @Override
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<Game> deleteById(final @PathVariable("Id") Integer id) {
        if (gameServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        gameServiceImpl.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
