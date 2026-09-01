<script setup lang="ts">
import { computed } from 'vue'

interface Point {
  label: string
  value: number
}

const props = withDefaults(
  defineProps<{
    points: Point[]
    size?: number
  }>(),
  {
    size: 260,
  },
)

const CENTER = 100
const RADIUS = 76
const RINGS = [0.25, 0.5, 0.75, 1]

const axisCount = computed(() => props.points.length)

function angleFor(i: number): number {
  return (Math.PI * 2 * i) / axisCount.value - Math.PI / 2
}

function vertexAt(i: number, ratio: number): { x: number; y: number } {
  const a = angleFor(i)
  return {
    x: CENTER + Math.cos(a) * RADIUS * ratio,
    y: CENTER + Math.sin(a) * RADIUS * ratio,
  }
}

const ringPaths = computed(() =>
  RINGS.map((r) =>
    props.points
      .map((_, i) => {
        const v = vertexAt(i, r)
        return `${v.x},${v.y}`
      })
      .join(' '),
  ),
)

const axisLines = computed(() =>
  props.points.map((_, i) => vertexAt(i, 1)),
)

const dataPath = computed(() =>
  props.points
    .map((p, i) => {
      const v = vertexAt(i, Math.min(1, Math.max(0, p.value / 100)))
      return `${v.x},${v.y}`
    })
    .join(' '),
)

const dataDots = computed(() =>
  props.points.map((p, i) => vertexAt(i, Math.min(1, Math.max(0, p.value / 100)))),
)

const labelPositions = computed(() =>
  props.points.map((p, i) => ({ ...vertexAt(i, 1.24), label: p.label, value: p.value })),
)
</script>

<template>
  <div class="radar" :style="{ width: `${size}px`, height: `${size}px` }">
    <svg viewBox="0 0 200 200" class="radar__svg">
      <defs>
        <radialGradient id="radarFill" cx="50%" cy="50%" r="50%">
          <stop offset="0%" stop-color="var(--lime)" stop-opacity="0.32" />
          <stop offset="100%" stop-color="var(--lime)" stop-opacity="0.04" />
        </radialGradient>
      </defs>

      <circle :cx="CENTER" :cy="CENTER" :r="RADIUS" class="radar__sweep-clip" />

      <polygon
        v-for="(ring, idx) in ringPaths"
        :key="idx"
        :points="ring"
        class="radar__ring"
      />
      <line
        v-for="(pt, i) in axisLines"
        :key="i"
        :x1="CENTER"
        :y1="CENTER"
        :x2="pt.x"
        :y2="pt.y"
        class="radar__axis"
      />

      <polygon :points="dataPath" class="radar__data" />
      <circle
        v-for="(d, i) in dataDots"
        :key="i"
        :cx="d.x"
        :cy="d.y"
        r="2.6"
        class="radar__dot"
      />
    </svg>

    <span
      v-for="(l, i) in labelPositions"
      :key="i"
      class="radar__label mono"
      :style="{ left: `${(l.x / 200) * 100}%`, top: `${(l.y / 200) * 100}%` }"
    >
      <span class="radar__label-text">{{ l.label }}</span>
      <span class="radar__label-value">{{ l.value }}</span>
    </span>
  </div>
</template>

<style scoped>
.radar {
  position: relative;
}

.radar__svg {
  width: 100%;
  height: 100%;
  overflow: visible;
}

.radar__sweep-clip {
  fill: rgba(69, 224, 230, 0.02);
}

.radar__ring {
  fill: none;
  stroke: var(--line-subtle);
  stroke-width: 1;
}

.radar__axis {
  stroke: var(--line-faint);
  stroke-width: 1;
}

.radar__data {
  fill: url(#radarFill);
  stroke: var(--lime);
  stroke-width: 1.5;
  stroke-linejoin: round;
  filter: drop-shadow(0 0 6px var(--lime-glow));
}

.radar__dot {
  fill: var(--bg-void);
  stroke: var(--lime);
  stroke-width: 1.5;
}

.radar__label {
  position: absolute;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  white-space: nowrap;
  pointer-events: none;
}

.radar__label-text {
  font-size: 9px;
  letter-spacing: 0.08em;
  color: var(--text-secondary);
}

.radar__label-value {
  font-size: 11px;
  font-weight: 700;
  color: var(--lime);
}
</style>
