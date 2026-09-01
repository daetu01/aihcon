import type { MatchRecord, PlayerProfile, SquadRecommendation } from '../types/pubg'
import { generateMatchHistory, generatePlayerProfile } from '../data/mockGenerator'
import { generateSquadRecommendation } from '../data/squadMock'

/**
 * Mock-backed client for the PUBG analysis API.
 *
 * Every function below is written against the endpoint shape the Spring Boot
 * backend will expose. Swap the mock body for `fetch(...)` once the backend
 * is live — the call sites in views/components do not need to change.
 */

const MOCK_LATENCY_MS = 260

function delay<T>(value: T, ms = MOCK_LATENCY_MS): Promise<T> {
  return new Promise((resolve) => setTimeout(() => resolve(value), ms))
}

function assertNickname(nickname: string): void {
  if (!nickname || !nickname.trim()) {
    throw new Error('PLAYER NAME REQUIRED')
  }
}

// const API_BASE = import.meta.env.VITE_API_BASE_URL ?? '/api'

/** GET /api/pubg/players/{nickname}/profile */
export async function fetchPlayerProfile(nickname: string): Promise<PlayerProfile> {
  assertNickname(nickname)
  // return fetch(`${API_BASE}/pubg/players/${encodeURIComponent(nickname)}/profile`).then((r) => r.json())
  return delay(generatePlayerProfile(nickname))
}

/** GET /api/pubg/players/{nickname}/matches */
export async function fetchPlayerMatches(nickname: string, limit = 28): Promise<MatchRecord[]> {
  assertNickname(nickname)
  // return fetch(`${API_BASE}/pubg/players/${encodeURIComponent(nickname)}/matches?limit=${limit}`).then((r) => r.json())
  return delay(generateMatchHistory(nickname, limit))
}

/** GET /api/squads/recommend?player={nickname} */
export async function fetchSquadRecommendation(nickname: string, variant = 0): Promise<SquadRecommendation> {
  assertNickname(nickname)
  const profile = generatePlayerProfile(nickname)
  // return fetch(`${API_BASE}/squads/recommend?player=${encodeURIComponent(nickname)}&variant=${variant}`).then((r) => r.json())
  return delay(generateSquadRecommendation(nickname, profile, variant))
}
