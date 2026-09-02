import type { SquadAnalyzeRequest, SquadAnalyzeResponse } from '../types/squad'

/**
 * Real client for the Spring Boot squad-analysis endpoint.
 * In dev, `/api` is proxied to the backend by vite.config.ts.
 */
const API_BASE = import.meta.env.VITE_API_BASE_URL ?? '/api'

/** POST /api/squads/analyze */
export async function analyzeSquad(nicknames: string[]): Promise<SquadAnalyzeResponse> {
  if (nicknames.length !== 4 || nicknames.some((n) => !n.trim())) {
    throw new Error('4명의 플레이어 닉네임이 모두 필요합니다.')
  }

  const body: SquadAnalyzeRequest = { nicknames }

  let res: Response
  try {
    res = await fetch(`${API_BASE}/squads/analyze`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
  } catch {
    throw new Error('서버에 연결할 수 없습니다. 백엔드 서버 상태를 확인해주세요.')
  }

  if (!res.ok) {
    const message = await res.text().catch(() => '')
    throw new Error(message || `스쿼드 분석에 실패했습니다. (HTTP ${res.status})`)
  }

  return res.json()
}
