package com.example.demo.dto;

public class UserContextResponse {
    private String userId;
    private String gameId;
    private RankInfo current;
    private RankInfo above;
    private RankInfo below;
    
    public UserContextResponse() {}
    
    public UserContextResponse(String userId, String gameId, RankInfo current, RankInfo above, RankInfo below) {
        this.userId = userId;
        this.gameId = gameId;
        this.current = current;
        this.above = above;
        this.below = below;
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
    
    public RankInfo getCurrent() {
        return current;
    }
    
    public void setCurrent(RankInfo current) {
        this.current = current;
    }
    
    public RankInfo getAbove() {
        return above;
    }
    
    public void setAbove(RankInfo above) {
        this.above = above;
    }
    
    public RankInfo getBelow() {
        return below;
    }
    
    public void setBelow(RankInfo below) {
        this.below = below;
    }
}
