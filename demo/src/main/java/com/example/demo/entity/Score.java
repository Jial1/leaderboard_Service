package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "scores", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "gameId"})
})
public class Score {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String gameId;
    
    @Column(nullable = false)
    private Long score;
    
    public Score() {}
    
    public Score(String userId, String gameId, Long score) {
        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
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
