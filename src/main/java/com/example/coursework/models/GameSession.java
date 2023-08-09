package com.example.coursework.models;

import com.example.coursework.models.enams.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession implements Entity {
    private static final String HEADERS = "sessionId, statusOfGame, startGame, endGame, userId, gameId";
    private Integer sessionId;
    private Status statusOfGame;
    private String startGame;
    private String endGame;
    private Integer userId;
    private Integer gameId;

    public static String getHeaders() {
        return HEADERS;
    }

    public String toCsv() {
        return getSessionId() + ", " + getStatusOfGame() + ", "
                + getStartGame() + ", " + getEndGame() + ", "
                + getUserId() + ", " + getGameId();
    }
}
