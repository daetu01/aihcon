export type PlayRole =
  | 'TACTICAL_SUPPORT'
  | 'ENTRY_FRAGGER'
  | 'SHOT_CALLER'
  | 'FLEX_SCOUT'

export const ROLE_LABEL: Record<PlayRole, string> = {
  TACTICAL_SUPPORT: 'TACTICAL SUPPORT',
  ENTRY_FRAGGER: 'ENTRY FRAGGER',
  SHOT_CALLER: 'SHOT CALLER',
  FLEX_SCOUT: 'FLEX SCOUT',
}

export const ROLE_DESC_KO: Record<PlayRole, string> = {
  TACTICAL_SUPPORT:
    '교전을 직접 시작하기보다 팀원의 교전에 빠르게 합류하며, 높은 생존율과 지원 행동을 기반으로 후방에서 전투 흐름을 안정화하는 유형입니다.',
  ENTRY_FRAGGER:
    '교전 지역에 가장 먼저 진입해 첫 킬을 만들어내는 유형입니다. 공격성과 기동성이 높고, 팀의 전투 개시를 주도합니다.',
  SHOT_CALLER:
    '전황을 읽고 팀의 이동과 교전 타이밍을 지시하는 유형입니다. 생존율과 팀플레이 지표가 고르게 높습니다.',
  FLEX_SCOUT:
    '고정된 포지션 없이 상황에 따라 정찰과 측면 지원을 오가는 유형입니다. 기동성이 뛰어나고 다양한 역할 전환에 강합니다.',
}

/** Aggregate play-style scores, 0-100. Mirrors the AI profiling output. */
export interface PlayStyleScores {
  aggression: number
  survival: number
  teamplay: number
  mobility: number
  combatInitiative: number
}

export interface PlayerSummary {
  nickname: string
  matchesAnalyzed: number
  avgDamage: number
  kd: number
  avgSurvivalTimeMin: number
  top10Rate: number
}

export interface PlayerAnalysis {
  role: PlayRole
  description: string
  strengths: string[]
  weaknesses: string[]
  recommendedRoles: PlayRole[]
}

/** GET /api/pubg/players/{nickname}/profile */
export interface PlayerProfile {
  summary: PlayerSummary
  scores: PlayStyleScores
  analysis: PlayerAnalysis
}

export type GameMode = 'SQUAD' | 'DUO' | 'SOLO' | 'SQUAD-FPP' | 'DUO-FPP' | 'SOLO-FPP'

/** One row of GET /api/pubg/players/{nickname}/matches */
export interface MatchRecord {
  id: string
  mode: GameMode
  map: string
  rank: number
  totalPlayers: number
  kills: number
  damage: number
  survivalTimeMin: number
  date: string // ISO 8601
}

export interface SquadMember {
  nickname: string
  role: PlayRole
  scores: PlayStyleScores
  matchPercent: number
  isYou?: boolean
}

/** GET /api/squads/recommend?player={nickname} */
export interface SquadRecommendation {
  synergyScore: number
  members: SquadMember[]
  reasons: string[]
}
