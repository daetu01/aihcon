/** Deterministic PRNG so the same nickname always yields the same mock profile. */
export function hashSeed(input: string): number {
  let h = 2166136261
  for (let i = 0; i < input.length; i++) {
    h ^= input.charCodeAt(i)
    h = Math.imul(h, 16777619)
  }
  return h >>> 0
}

export function mulberry32(seed: number): () => number {
  let a = seed
  return () => {
    a |= 0
    a = (a + 0x6d2b79f5) | 0
    let t = Math.imul(a ^ (a >>> 15), 1 | a)
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296
  }
}

export function createRng(seedStr: string): () => number {
  return mulberry32(hashSeed(seedStr))
}

export function randInt(rng: () => number, min: number, max: number): number {
  return Math.floor(rng() * (max - min + 1)) + min
}

export function randFloat(rng: () => number, min: number, max: number, precision = 1): number {
  const v = rng() * (max - min) + min
  const p = 10 ** precision
  return Math.round(v * p) / p
}

export function pick<T>(rng: () => number, items: readonly T[]): T {
  return items[Math.floor(rng() * items.length)]
}

export function pickN<T>(rng: () => number, items: readonly T[], n: number): T[] {
  const pool = [...items]
  const out: T[] = []
  while (out.length < n && pool.length > 0) {
    const idx = Math.floor(rng() * pool.length)
    out.push(pool.splice(idx, 1)[0])
  }
  return out
}

export function clamp(v: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, v))
}
