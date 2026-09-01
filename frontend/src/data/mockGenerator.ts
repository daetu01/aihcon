import type {
  GameMode,
  MatchRecord,
  PlayerAnalysis,
  PlayerProfile,
  PlayerSummary,
  PlayRole,
  PlayStyleScores,
} from '../types/pubg'
import { createRng, randFloat, randInt, clamp, pickN, pick } from './random'
import { ALL_ROLES, ROLE_CENTER, ROLE_STRENGTHS, ROLE_WEAKNESSES, ROLE_RECOMMENDED, ROLE_DESCRIPTION } from './roleBank'

const MAPS = ['Erangel', 'Miramar', 'Sanhok', 'Vikendi', 'Taego', 'Deston', 'Rondo']
const MODES: GameMode[] = ['SQUAD-FPP', 'SQUAD', 'DUO-FPP', 'SOLO-FPP']

function jitterScores(rng: () => number, role: PlayRole): PlayStyleScores {
  const center = ROLE_CENTER[role]
  const jitter = (v: number) => clamp(Math.round(v + randFloat(rng, -11, 11)), 5, 98)
  return {
    aggression: jitter(center.aggression),
    survival: jitter(center.survival),
    teamplay: jitter(center.teamplay),
    mobility: jitter(center.mobility),
    combatInitiative: jitter(center.combatInitiative),
  }
}

function buildSummary(rng: () => number, nickname: string, scores: PlayStyleScores): PlayerSummary {
  const aggressionFactor = (scores.aggression + scores.combatInitiative) / 200
  const survivalFactor = scores.survival / 100
  return {
    nickname,
    matchesAnalyzed: randInt(rng, 18, 32),
    avgDamage: Math.round(180 + aggressionFactor * 420 + randFloat(rng, -20, 20)),
    kd: clamp(randFloat(rng, 0.9, 1.8, 2) + aggressionFactor * 2.3, 0.6, 5.2),
    avgSurvivalTimeMin: clamp(randFloat(rng, 12, 17, 1) + survivalFactor * 10, 10, 28),
    top10Rate: clamp(Math.round(22 + survivalFactor * 45 + randFloat(rng, -6, 6)), 8, 82),
  }
}

function buildAnalysis(rng: () => number, role: PlayRole): PlayerAnalysis {
  return {
    role,
    description: ROLE_DESCRIPTION[role],
    strengths: pickN(rng, ROLE_STRENGTHS[role], 3),
    weaknesses: pickN(rng, ROLE_WEAKNESSES[role], 2),
    recommendedRoles: ROLE_RECOMMENDED[role],
  }
}

/** Canonical fixture used throughout the spec's example copy. */
const DAEAN_SCORES: PlayStyleScores = {
  aggression: 78,
  survival: 86,
  teamplay: 91,
  mobility: 64,
  combatInitiative: 52,
}

function isCanonicalPlayer(nickname: string): boolean {
  return nickname.trim().toLowerCase() === 'daean123'
}

export function generatePlayerProfile(nicknameRaw: string): PlayerProfile {
  const nickname = nicknameRaw.trim()
  const rng = createRng(nickname.toLowerCase())

  if (isCanonicalPlayer(nickname)) {
    const scores = DAEAN_SCORES
    return {
      summary: {
        nickname,
        matchesAnalyzed: 28,
        avgDamage: 312,
        kd: 2.4,
        avgSurvivalTimeMin: 21.4,
        top10Rate: 58,
      },
      scores,
      analysis: {
        role: 'TACTICAL_SUPPORT',
        description: ROLE_DESCRIPTION.TACTICAL_SUPPORT,
        strengths: ['High team contribution', 'Strong late-game survival', 'Reliable combat support'],
        weaknesses: ['Low early engagement', 'Low combat initiation'],
        recommendedRoles: ROLE_RECOMMENDED.TACTICAL_SUPPORT,
      },
    }
  }

  const role = pick(rng, ALL_ROLES)
  const scores = jitterScores(rng, role)
  return {
    summary: buildSummary(rng, nickname, scores),
    scores,
    analysis: buildAnalysis(rng, role),
  }
}

export function generateMatchHistory(nicknameRaw: string, count: number): MatchRecord[] {
  const nickname = nicknameRaw.trim()
  const rng = createRng(`${nickname.toLowerCase()}::matches`)
  const now = new Date('2026-09-01T09:00:00Z')

  const matches: MatchRecord[] = []
  for (let i = 0; i < count; i++) {
    const totalPlayers = 96
    const survivalTimeMin = randFloat(rng, 3, 32, 1)
    const rank = survivalTimeMin > 20 ? randInt(rng, 1, 10) : randInt(rng, 8, totalPlayers)
    const date = new Date(now.getTime() - i * randInt(rng, 3, 14) * 60 * 60 * 1000)
    matches.push({
      id: `${nickname.toLowerCase()}-m${count - i}`,
      mode: pick(rng, MODES),
      map: pick(rng, MAPS),
      rank,
      totalPlayers,
      kills: rank <= 15 ? randInt(rng, 1, 9) : randInt(rng, 0, 4),
      damage: randInt(rng, 40, 620),
      survivalTimeMin,
      date: date.toISOString(),
    })
  }
  return matches
}
