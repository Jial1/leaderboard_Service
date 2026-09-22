package com.example.demo.service;

import com.example.demo.dto.ScoreSubmissionResponse;
import com.example.demo.dto.TopRankingsResponse;
import com.example.demo.dto.UserContextResponse;

public interface LeaderboardService {
    ScoreSubmissionResponse submitScore(String userId, String gameId, Long score);
    
    TopRankingsResponse getTopRankings(String gameId, Integer limit);
    
    UserContextResponse getUserContext(String userId, String gameId);
}
