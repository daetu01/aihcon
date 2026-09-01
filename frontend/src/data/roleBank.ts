import type { PlayRole, PlayStyleScores } from '../types/pubg'
import { ROLE_DESC_KO } from '../types/pubg'

export const ALL_ROLES: PlayRole[] = [
  'TACTICAL_SUPPORT',
  'ENTRY_FRAGGER',
  'SHOT_CALLER',
  'FLEX_SCOUT',
]

/** Archetype centers used to jitter-generate a coherent stat block per role. */
export const ROLE_CENTER: Record<PlayRole, PlayStyleScores> = {
  TACTICAL_SUPPORT: { aggression: 45, survival: 85, teamplay: 90, mobility: 60, combatInitiative: 48 },
  ENTRY_FRAGGER: { aggression: 88, survival: 60, teamplay: 60, mobility: 85, combatInitiative: 90 },
  SHOT_CALLER: { aggression: 62, survival: 88, teamplay: 92, mobility: 58, combatInitiative: 72 },
  FLEX_SCOUT: { aggression: 58, survival: 68, teamplay: 65, mobility: 92, combatInitiative: 62 },
}

export const ROLE_STRENGTHS: Record<PlayRole, string[]> = {
  TACTICAL_SUPPORT: [
    'High team contribution',
    'Strong late-game survival',
    'Reliable combat support',
    'Consistent positioning discipline',
  ],
  ENTRY_FRAGGER: [
    'High first-engagement success',
    'Aggressive zone control',
    'Strong close-range duels',
    'Fast rotation into fights',
  ],
  SHOT_CALLER: [
    'High situational awareness',
    'Consistent squad direction',
    'Strong late-game decision making',
    'Balanced engagement discipline',
  ],
  FLEX_SCOUT: [
    'High map mobility',
    'Fast information gathering',
    'Flexible role adaptation',
    'Strong flank rotations',
  ],
}

export const ROLE_WEAKNESSES: Record<PlayRole, string[]> = {
  TACTICAL_SUPPORT: ['Low early engagement', 'Low combat initiation', 'Passive rotation timing'],
  ENTRY_FRAGGER: ['High early-game risk exposure', 'Inconsistent late-game survival', 'Over-extension under pressure'],
  SHOT_CALLER: ['Lower solo aggression', 'Dependent on team coordination', 'Slower individual fragging pace'],
  FLEX_SCOUT: ['Lower sustained team contribution', 'Inconsistent engagement commitment', 'Lower average damage output'],
}

export const ROLE_RECOMMENDED: Record<PlayRole, PlayRole[]> = {
  TACTICAL_SUPPORT: ['ENTRY_FRAGGER', 'SHOT_CALLER', 'FLEX_SCOUT'],
  ENTRY_FRAGGER: ['TACTICAL_SUPPORT', 'SHOT_CALLER', 'FLEX_SCOUT'],
  SHOT_CALLER: ['ENTRY_FRAGGER', 'TACTICAL_SUPPORT', 'FLEX_SCOUT'],
  FLEX_SCOUT: ['TACTICAL_SUPPORT', 'SHOT_CALLER', 'ENTRY_FRAGGER'],
}

export const ROLE_DESCRIPTION = ROLE_DESC_KO
