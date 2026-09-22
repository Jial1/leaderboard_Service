package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ScoreSubmissionRequest {
    @NotBlank(message = "gameId is required and cannot be blank")
    private String gameId;

    @NotNull(message = "score is required")
    @PositiveOrZero(message = "score cannot be negative")
    private Long score;

    public ScoreSubmissionRequest() {}

    public ScoreSubmissionRequest(String gameId, Long score) {
        this.gameId = gameId;
        this.score = score;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
