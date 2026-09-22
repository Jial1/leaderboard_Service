package com.example.demo.service;

import com.example.demo.dto.ScoreSubmissionResponse;
import com.example.demo.dto.TopRankingsResponse;
import com.example.demo.dto.UserContextResponse;
import com.example.demo.entity.Score;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class LeaderboardServiceTest {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @BeforeEach
    void setUp() {
        scoreRepository.deleteAll();
    }
    
    @Test
    void testSubmitScore_Success() {
        ScoreSubmissionResponse response = leaderboardService.submitScore("user1", "game1", 1000L);
        
        assertNotNull(response);
        assertEquals("game1", response.getGameId());
        assertEquals(1000L, response.getScore());
        
        // Verify persistence
        assertTrue(scoreRepository.findByUserIdAndGameId("user1", "game1").isPresent());
        Score saved = scoreRepository.findByUserIdAndGameId("user1", "game1").get();
        assertEquals("user1", saved.getUserId());
        assertEquals("game1", saved.getGameId());
        assertEquals(1000L, saved.getScore());
    }
    
    @Test
    void testSubmitScore_ReplaceExistingScore() {
        // First submission
        leaderboardService.submitScore("user1", "game1", 1000L);
        
        // Second submission - should replace
        ScoreSubmissionResponse response = leaderboardService.submitScore("user1", "game1", 2000L);
        
        assertEquals(2000L, response.getScore());
        assertEquals(1, scoreRepository.findByGameId("game1").size());
        assertEquals(2000L, scoreRepository.findByUserIdAndGameId("user1", "game1").get().getScore());
    }
    
    @Test
    void testSubmitScore_ValidateUserIdNull() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore(null, "game1", 1000L);
        });
    }
    
    @Test
    void testSubmitScore_ValidateUserIdBlank() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore("", "game1", 1000L);
        });
    }
    
    @Test
    void testSubmitScore_ValidateGameIdNull() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore("user1", null, 1000L);
        });
    }
    
    @Test
    void testSubmitScore_ValidateGameIdBlank() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore("user1", "", 1000L);
        });
    }
    
    @Test
    void testSubmitScore_ValidateScoreNull() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore("user1", "game1", null);
        });
    }
    
    @Test
    void testSubmitScore_ValidateScoreNegative() {
        assertThrows(ValidationException.class, () -> {
            leaderboardService.submitScore("user1", "game1", -100L);
        });
    }
    
    @Test
    void testGetTopRankings_SortsByScoreDescAndUserId() {
        leaderboardService.submitScore("user2", "game1", 100L);
        leaderboardService.submitScore("user1", "game1", 100L);
        leaderboardService.submitScore("user3", "game1", 50L);

        TopRankingsResponse response = leaderboardService.getTopRankings("game1", 3);

        assertEquals("game1", response.getGameId());
        assertEquals(3, response.getRankings().size());
        assertEquals("user1", response.getRankings().get(0).getUserId());
        assertEquals("user2", response.getRankings().get(1).getUserId());
        assertEquals("user3", response.getRankings().get(2).getUserId());
    }

    @Test
    void testGetUserContext_ReturnsOneAboveAndOneBelow() {
        leaderboardService.submitScore("user1", "game1", 1000L);
        leaderboardService.submitScore("user2", "game1", 900L);
        leaderboardService.submitScore("user3", "game1", 800L);
        leaderboardService.submitScore("user4", "game1", 700L);

        UserContextResponse response = leaderboardService.getUserContext("user3", "game1");

        assertEquals("user3", response.getUserId());
        assertEquals("game1", response.getGameId());
        assertEquals(3, response.getCurrent().getRank());
        assertEquals("user2", response.getAbove().getUserId());
        assertEquals("user4", response.getBelow().getUserId());
    }

    @Test
    void testGetUserContext_UserNotFound() {
        leaderboardService.submitScore("user1", "game1", 1000L);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            leaderboardService.getUserContext("missing-user", "game1");
        });
        assertTrue(exception.getMessage().contains("User not found"));
    }
}
