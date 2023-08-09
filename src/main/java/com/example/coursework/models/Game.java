package com.example.coursework.models;

import com.example.coursework.models.enams.Games;
import com.example.coursework.models.enams.GenreOfGame;
import com.example.coursework.models.enams.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game  implements Entity {
    private static final String HEADERS = "gameId, nameOfGame, description, "
            + "reviews , genreOfGame, publisherId, usersId";
    private Integer gameId;
    private Games nameOfGame;
    private String description;
    private Review reviews;
    private GenreOfGame genreOfGame;
    private Integer publisherId;
    private List<Integer> usersId;

    public static String getHeaders() {
        return HEADERS;
    }

    public String toCsv() {
        return getGameId() + ", " + getNameOfGame() + ", "
                + getDescription() + ", " + getReviews() + ", "
                + getGenreOfGame() + ", " + getPublisherId() + ", " + getUsersId();
    }
}
