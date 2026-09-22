# Leaderboard Service - Implementation Specification

## 1. Requirements & Assumptions
- Score submissions **replace** prior score for user+game
- Global cross-game ranking (all games use single rank calculation)
- Tie-breaking: alphabetical by userId ascending
- Minimal disclosure: no userId, globalRank in score submission response
- MVP deployed within 1 hour
- In-memory H2 database for simplicity
- Score values are non-negative; negative scores are rejected with `400 Bad Request`

## 2. API Endpoints

| Endpoint | Method | Request | Response | Status |
|----------|--------|---------|----------|--------|
| `/api/users/{userId}/scores` | POST | `{gameId, score}` | `{gameId, score}` | 201 |
| `/api/rankings/top` | GET | `?limit={n}&gameId={gameId}` | `{gameId, limit, rankings[]}` | 200 |
| `/api/users/{userId}/rankings` | GET | `?gameId={gameId}` | `{userId, gameId, current, above, below}` | 200 |

## 3. Data Model
**Score Entity:**
- `userId` (String, not null)
- `gameId` (String, not null)
- `score` (Long, non-negative)
- Composite unique key: `(userId, gameId)`

## 4. Architecture & Responsibilities
- **Controller**: Parse HTTP requests/responses, delegate to service
- **Service**: Input validation, score replacement, ranking calculation, tie-breaking, user context calculation
- **Repository**: Query/persist Score entities
- **DB**: H2 in-memory, single `scores` table

## 5. Validation & Error Behavior
Current implementation behavior:
- `400 Bad Request`: invalid userId, gameId, score, malformed JSON, missing required request values, invalid query params
- `404 Not Found`: user or game not found in leaderboard context lookup
- `500 Internal Server Error`: unexpected exceptions only; generic message with no internal details
- All errors: `{error: "message"}`

Validated inputs currently enforced:
- `userId` must be non-null and not blank
- `gameId` must be non-null and not blank
- `score` must be non-null and `>= 0`
- `limit` must be greater than zero
- request body must be valid JSON and DTO validation must pass

## 6. Implementation Decisions
1. **Rankings computed in-memory post-query** (not DB-stored) for MVP simplicity
2. **In-memory H2 database** (eliminates external DB setup, fast startup)
3. **Stateless service layer** (no caching; recalculate per request)
4. **No user entity validation** (assume all userIds valid; can extend later)
5. **Above/below return null if at boundary** (rank 1 has no "above")
6. **Scores must be >= 0** (negative scores rejected with 400 Bad Request)
7. **Replace semantics** for user+game pair implemented by updating the existing score row instead of creating duplicates

## 7. Current Automated Test Plan
Implemented focused service-level validation covering the current MVP:
- Submit score happy path
- Score replacement for same user+game
- Invalid userId, gameId, score, and negative score
- Top rankings sort order by score desc and userId asc for ties
- User context returns one above and one below for a valid user
- User context returns `404` when user is absent from a leaderboard

This is intentionally minimal and high-value for current scope.

## 8. Discuss in Technical Review - DO NOT IMPLEMENT
- Query performance at scale (current: O(n log n) per request acceptable)
- Concurrent submission race conditions
- Caching strategy for heavy read workloads
- Database migration strategy for production persistence
