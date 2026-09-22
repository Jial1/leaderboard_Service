package com.example.demo.controller;

import com.example.demo.dto.ScoreSubmissionRequest;
import com.example.demo.dto.ScoreSubmissionResponse;
import com.example.demo.dto.TopRankingsResponse;
import com.example.demo.dto.UserContextResponse;
import com.example.demo.service.LeaderboardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class LeaderboardController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @PostMapping("/users/{userId}/scores")
    public ResponseEntity<ScoreSubmissionResponse> submitScore(
        @PathVariable String userId,
        @Valid @RequestBody ScoreSubmissionRequest request) {
        ScoreSubmissionResponse response = leaderboardService.submitScore(
            userId,
            request.getGameId(),
            request.getScore()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/rankings/top")
    public ResponseEntity<TopRankingsResponse> getTopRankings(
        @RequestParam @NotBlank(message = "gameId is required and cannot be blank") String gameId,
        @RequestParam @Positive(message = "limit must be greater than zero") Integer limit) {
        TopRankingsResponse response = leaderboardService.getTopRankings(gameId, limit);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{userId}/rankings")
    public ResponseEntity<UserContextResponse> getUserContext(
        @PathVariable String userId,
        @RequestParam @NotBlank(message = "gameId is required and cannot be blank") String gameId) {
        UserContextResponse response = leaderboardService.getUserContext(userId, gameId);
        return ResponseEntity.ok(response);
    }
}
