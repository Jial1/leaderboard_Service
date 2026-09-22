package com.example.demo.dto;

public class RankInfo {
    private Integer rank;
    private String userId;
    private Long score;
    
    public RankInfo() {}
    
    public RankInfo(Integer rank, String userId, Long score) {
        this.rank = rank;
        this.userId = userId;
        this.score = score;
    }
    
    public Integer getRank() {
        return rank;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public Long getScore() {
        return score;
    }
    
    public void setScore(Long score) {
        this.score = score;
    }
}
