import type { PlayerProfile, PlayRole, SquadMember, SquadRecommendation } from '../types/pubg'
import { createRng, randFloat, clamp, pickN } from './random'
import { ROLE_CENTER, ROLE_RECOMMENDED } from './roleBank'

const CANDIDATE_POOL: Record<PlayRole, string[]> = {
  ENTRY_FRAGGER: ['RAVEN_02', 'BLITZ_9', 'FALCONX', 'NOVA_RUSH'],
  SHOT_CALLER: ['HEX90', 'ORACLE_7', 'VANGUARD', 'CIPHER_K'],
  FLEX_SCOUT: ['VECTOR77', 'GHOSTLINE', 'ECHO_44', 'DRIFTER_X'],
  TACTICAL_SUPPORT: ['AEGIS_11', 'WARDEN_3', 'SENTRY_7', 'HOLDLINE'],
}

function jitteredMember(rng: () => number, role: PlayRole, nickname: string): SquadMember {
  const center = ROLE_CENTER[role]
  const jitter = (v: number) => clamp(Math.round(v + randFloat(rng, -9, 9)), 5, 98)
  return {
    nickname,
    role,
    scores: {
      aggression: jitter(center.aggression),
      survival: jitter(center.survival),
      teamplay: jitter(center.teamplay),
      mobility: jitter(center.mobility),
      combatInitiative: jitter(center.combatInitiative),
    },
    matchPercent: 0,
  }
}

function matchPercentFor(self: SquadMember, teammate: SquadMember): number {
  const teamplayGap = Math.abs(self.scores.teamplay - teammate.scores.teamplay)
  const survivalGap = Math.abs(self.scores.survival - teammate.scores.survival)
  const raw = 100 - teamplayGap * 0.28 - survivalGap * 0.18
  return clamp(Math.round(raw), 71, 98)
}

function roleReason(self: SquadMember, mate: SquadMember): string {
  switch (mate.role) {
    case 'ENTRY_FRAGGER':
      return `${mate.nickname}의 높은 교전 개시 성향이 ${self.nickname}의 안정적인 지원 성향을 보완합니다.`
    case 'SHOT_CALLER':
      return `${mate.nickname}은 높은 생존율과 적극적인 오더 성향을 보유하고 있어 팀의 전투 방향성을 안정적으로 유지합니다.`
    case 'FLEX_SCOUT':
      return `${mate.nickname}의 뛰어난 기동성은 스쿼드의 정찰 범위를 넓히고 측면 대응력을 강화합니다.`
    case 'TACTICAL_SUPPORT':
      return `${mate.nickname}의 안정적인 지원 성향이 ${self.nickname}의 공격적인 교전 스타일 뒤를 든든하게 받쳐줍니다.`
  }
}

function playtimeReason(members: SquadMember[]): string {
  const mobilities = members.map((m) => m.scores.mobility)
  const spread = Math.max(...mobilities) - Math.min(...mobilities)
  return spread < 18
    ? '네 플레이어의 평균 플레이 시간대와 이동 패턴이 유사합니다.'
    : '네 플레이어의 활동 반경이 서로 달라 넓은 지역을 효율적으로 커버할 수 있습니다.'
}

function isCanonicalPlayer(nickname: string): boolean {
  return nickname.trim().toLowerCase() === 'daean123'
}

export function generateSquadRecommendation(
  nicknameRaw: string,
  profile: PlayerProfile,
  variant = 0,
): SquadRecommendation {
  const nickname = nicknameRaw.trim()
  const you: SquadMember = {
    nickname,
    role: profile.analysis.role,
    scores: profile.scores,
    matchPercent: 100,
    isYou: true,
  }

  if (isCanonicalPlayer(nickname) && variant === 0) {
    const raven: SquadMember = {
      nickname: 'RAVEN_02',
      role: 'ENTRY_FRAGGER',
      scores: { aggression: 92, survival: 61, teamplay: 58, mobility: 87, combatInitiative: 94 },
      matchPercent: 94,
    }
    const hex: SquadMember = {
      nickname: 'HEX90',
      role: 'SHOT_CALLER',
      scores: { aggression: 65, survival: 89, teamplay: 93, mobility: 55, combatInitiative: 74 },
      matchPercent: 91,
    }
    const vector: SquadMember = {
      nickname: 'VECTOR77',
      role: 'FLEX_SCOUT',
      scores: { aggression: 57, survival: 66, teamplay: 63, mobility: 95, combatInitiative: 60 },
      matchPercent: 90,
    }
    return {
      synergyScore: 92,
      members: [you, raven, hex, vector],
      reasons: [
        'RAVEN_02의 높은 교전 개시 성향이 DAEAN123의 안정적인 지원 성향을 보완합니다.',
        'HEX90은 높은 생존율과 적극적인 오더 성향을 보유하고 있어 팀의 전투 방향성을 안정적으로 유지합니다.',
        '네 플레이어의 평균 플레이 시간이 유사합니다.',
      ],
    }
  }

  const rng = createRng(`${nickname.toLowerCase()}::squad::${variant}`)
  const neededRoles = ROLE_RECOMMENDED[profile.analysis.role]
  const teammates = neededRoles.map((role) => {
    const nick = pickN(rng, CANDIDATE_POOL[role], 1)[0]
    return jitteredMember(rng, role, nick)
  })

  teammates.forEach((m) => {
    m.matchPercent = matchPercentFor(you, m)
  })

  const members = [you, ...teammates]
  const synergyScore = clamp(
    Math.round(teammates.reduce((sum, m) => sum + m.matchPercent, 0) / teammates.length),
    70,
    97,
  )

  const reasons = [...teammates.map((m) => roleReason(you, m)), playtimeReason(members)]

  return { synergyScore, members, reasons }
}
