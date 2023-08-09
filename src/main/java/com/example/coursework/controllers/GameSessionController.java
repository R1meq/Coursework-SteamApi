package com.example.coursework.controllers;

import com.example.coursework.models.GameSession;
import org.springframework.http.ResponseEntity;

public interface GameSessionController extends ControllerTemplate<Integer, GameSession> {
    ResponseEntity<GameSession> startGame(Integer id);

    ResponseEntity<GameSession> endGame(Integer id);
}
