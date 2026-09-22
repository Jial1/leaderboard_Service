package com.example.demo.dto;

import java.util.List;

public class TopRankingsResponse {
    private String gameId;
    private Integer limit;
    private List<RankInfo> rankings;
    
    public TopRankingsResponse() {}
    
    public TopRankingsResponse(String gameId, Integer limit, List<RankInfo> rankings) {
        this.gameId = gameId;
        this.limit = limit;
        this.rankings = rankings;
    }
    
    public String getGameId() {
        return gameId;
    }
    
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public List<RankInfo> getRankings() {
        return rankings;
    }
    
    public void setRankings(List<RankInfo> rankings) {
        this.rankings = rankings;
    }
}
