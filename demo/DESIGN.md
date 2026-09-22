# Leaderboard Service - Implementation Specification

## 1. Requirements & Assumptions
- Score submissions **replace** prior score for user+game
- Global cross-game ranking (all games use single rank calculation)
- Tie-breaking: alphabetical by userId ascending
- Minimal disclosure: no userId, globalRank in score submission response
- MVP deployed within 1 hour
- In-memory H2 database for simplicity

## 2. API Endpoints

| Endpoint | Method | Request | Response | Status |
|----------|--------|---------|----------|--------|
| `/users/{userId}/scores` | POST | `{gameId, score}` | `{gameId, score}` | 201 |
| `/rankings/top` | GET | `?limit={n}&gameId={gameId}` | `{gameId, limit, rankings[]}` | 200 |
| `/users/{userId}/rankings` | GET | `?gameId={gameId}` | `{userId, gameId, current, above, below}` | 200 |

## 3. Data Model
**Score Entity:**
- `userId` (String, not null)
- `gameId` (String, not null)
- `score` (Long)
- Composite unique key: `(userId, gameId)`

## 4. Architecture & Responsibilities
- **Controller**: Parse HTTP requests/responses, delegate to service
- **Service**: Rank calculation logic, tie-breaking, data retrieval orchestration
- **Repository**: Query/persist Score entities
- **DB**: H2 in-memory, single `scores` table

## 5. Validation & Error Behavior
- `400 Bad Request`: Invalid userId, gameId, or score
- `404 Not Found`: User/game not found (or return empty ranking)
- `500 Internal Server Error`: Unexpected exceptions
- All errors: `{error: "message"}`

## 6. Implementation Decisions
1. **Rankings computed in-memory post-query** (not DB-stored) for MVP simplicity
2. **In-memory H2 database** (eliminates external DB setup, fast startup)
3. **Stateless service layer** (no caching; recalculate per request)
4. **No user entity validation** (assume all userIds valid; can extend later)
5. **Above/below return null if at boundary** (rank 1 has no "above")
6. **Scores must be >= 0** (negative scores rejected with 400 Bad Request)

## 7. Test Plan
- Unit: Service rank calculation (tie-breaking, boundaries)
- Integration: POST score → GET rankings reflects update
- Integration: Top X returns correct sorted order with ties
- Integration: User context returns correct neighbors
- Manual: Deploy and test all 3 endpoints with sample data

## 8. Discuss in Technical Review - DO NOT IMPLEMENT
- Query performance at scale (current: O(n log n) per request acceptable)
- Concurrent submission race conditions
- Caching strategy for heavy read workloads
