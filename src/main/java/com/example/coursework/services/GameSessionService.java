package com.example.coursework.services;

import com.example.coursework.models.GameSession;

public interface GameSessionService extends ServiceTemplate<Integer, GameSession> {
    GameSession startGame(Integer id);

    GameSession endGame(Integer id);

}