package com.example.demo.service;

import com.example.demo.dto.RankInfo;
import com.example.demo.dto.ScoreSubmissionResponse;
import com.example.demo.dto.TopRankingsResponse;
import com.example.demo.dto.UserContextResponse;
import com.example.demo.entity.Score;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @Override
    public ScoreSubmissionResponse submitScore(String userId, String gameId, Long score) {
        // Validate inputs
        if (userId == null || userId.isBlank()) {
            throw new ValidationException("userId is required and cannot be blank");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new ValidationException("gameId is required and cannot be blank");
        }
        if (score == null) {
            throw new ValidationException("score is required");
        }
        if (score < 0) {
            throw new ValidationException("score cannot be negative");
        }
        
        // Find existing score or create new
        Score scoreEntity = scoreRepository.findByUserIdAndGameId(userId, gameId)
            .orElse(new Score(userId, gameId, score));
        
        // Update score (replace semantics)
        scoreEntity.setScore(score);
        
        // Persist
        scoreRepository.save(scoreEntity);
        
        // Return response (no userId or globalRank disclosed)
        return new ScoreSubmissionResponse(gameId, score);
    }
    
    @Override
    public TopRankingsResponse getTopRankings(String gameId, Integer limit) {
        if (gameId == null || gameId.isBlank()) {
            throw new ValidationException("gameId is required and cannot be blank");
        }
        if (limit == null || limit <= 0) {
            throw new ValidationException("limit must be greater than zero");
        }

        List<Score> scores = scoreRepository.findByGameId(gameId);
        scores.sort(Comparator.comparing(Score::getScore, Comparator.reverseOrder())
            .thenComparing(Score::getUserId));

        List<RankInfo> rankings = new ArrayList<>();
        int maxEntries = Math.min(limit, scores.size());
        for (int i = 0; i < maxEntries; i++) {
            Score score = scores.get(i);
            rankings.add(new RankInfo(i + 1, score.getUserId(), score.getScore()));
        }

        return new TopRankingsResponse(gameId, limit, rankings);
    }
    
    @Override
    public UserContextResponse getUserContext(String userId, String gameId) {
        if (userId == null || userId.isBlank()) {
            throw new ValidationException("userId is required and cannot be blank");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new ValidationException("gameId is required and cannot be blank");
        }

        List<Score> scores = scoreRepository.findByGameId(gameId);
        if (scores.isEmpty()) {
            throw new NotFoundException("Game not found in leaderboard: " + gameId);
        }

        scores.sort(Comparator.comparing(Score::getScore, Comparator.reverseOrder())
            .thenComparing(Score::getUserId));

        List<RankInfo> rankingList = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            rankingList.add(new RankInfo(i + 1, score.getUserId(), score.getScore()));
        }

        int currentIndex = -1;
        for (int i = 0; i < rankingList.size(); i++) {
            if (rankingList.get(i).getUserId().equals(userId)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex < 0) {
            throw new NotFoundException("User not found in leaderboard for gameId: " + gameId);
        }

        RankInfo current = rankingList.get(currentIndex);
        RankInfo above = currentIndex > 0 ? rankingList.get(currentIndex - 1) : null;
        RankInfo below = currentIndex < rankingList.size() - 1 ? rankingList.get(currentIndex + 1) : null;

        return new UserContextResponse(userId, gameId, current, above, below);
    }
}
