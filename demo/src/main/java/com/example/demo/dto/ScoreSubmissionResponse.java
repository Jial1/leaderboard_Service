package com.example.demo.dto;

public class ScoreSubmissionResponse {
    private String gameId;
    private Long score;
    
    public ScoreSubmissionResponse() {}
    
    public ScoreSubmissionResponse(String gameId, Long score) {
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
