/** POST /api/squads/analyze */
export interface SquadAnalyzeRequest {
  nicknames: string[]
}

export interface PlayerInfo {
  nickname: string
  playStyle: string
  aggression: number
  combat: number
  mobility: number
  support: number
  survival: number
}

export interface TeamProfile {
  aggression: number
  combat: number
  mobility: number
  support: number
  survival: number
}

export interface SquadAnalyzeResponse {
  compatibilityScore: number
  players: PlayerInfo[]
  strengths: string[]
  weaknesses: string[]
  teamProfile: TeamProfile
}

export type PlayStyle =
  | 'ENTRY_FRAGGER'
  | 'SUPPORT'
  | 'SURVIVOR'
  | 'SCOUT'
  | 'SHARPSHOOTER'
  | 'FLEX'
  | 'INSUFFICIENT_DATA'

const PLAY_STYLE_LABEL: Record<PlayStyle, string> = {
  ENTRY_FRAGGER: 'ENTRY FRAGGER',
  SUPPORT: 'SUPPORT',
  SURVIVOR: 'SURVIVOR',
  SCOUT: 'SCOUT',
  SHARPSHOOTER: 'SHARPSHOOTER',
  FLEX: 'FLEX',
  INSUFFICIENT_DATA: 'INSUFFICIENT DATA',
}

const PLAY_STYLE_BADGE: Record<PlayStyle, string> = {
  ENTRY_FRAGGER: 'badge--lime',
  SUPPORT: 'badge--cyan',
  SURVIVOR: 'badge--amber',
  SCOUT: 'badge--violet',
  SHARPSHOOTER: 'badge--red',
  FLEX: '',
  INSUFFICIENT_DATA: 'badge--dim',
}

export function playStyleLabel(style: string): string {
  return PLAY_STYLE_LABEL[style as PlayStyle] ?? style.replace(/_/g, ' ')
}

export function playStyleBadgeClass(style: string): string {
  return PLAY_STYLE_BADGE[style as PlayStyle] ?? ''
}
