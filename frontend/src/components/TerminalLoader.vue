<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { generatePlayerProfile } from '../data/mockGenerator'

const props = withDefaults(
  defineProps<{
    nickname: string
    durationMs?: number
  }>(),
  {
    durationMs: 2400,
  },
)

const emit = defineEmits<{ complete: [] }>()

const matchCount = computed(() => generatePlayerProfile(props.nickname).summary.matchesAnalyzed)

const BAR_WIDTH = 22
const STEPS = [
  'ANALYZING COMBAT PATTERNS',
  'ANALYZING MOVEMENT',
  'ANALYZING SURVIVAL BEHAVIOR',
  'BUILDING PLAYER PROFILE',
]

const logLines = computed(() => [
  '> connecting to PUBG data node...',
  '> retrieving match history...',
  `> ${matchCount.value} matches located`,
  '> extracting player behavior...',
  '> profile generated',
])

const elapsed = ref(0)
const visibleLogCount = ref(0)
const stepDoneCount = ref(0)
let raf: number | undefined
let start = 0

const progress = computed(() => Math.min(100, Math.round((elapsed.value / props.durationMs) * 100)))
const barFilled = computed(() => Math.round((progress.value / 100) * BAR_WIDTH))
const barText = computed(() => `[${'█'.repeat(barFilled.value)}${'-'.repeat(BAR_WIDTH - barFilled.value)}]`)

function tick(timestamp: number) {
  if (!start) start = timestamp
  elapsed.value = timestamp - start

  const logThreshold = props.durationMs / (logLines.value.length + 1)
  visibleLogCount.value = Math.min(logLines.value.length, Math.floor(elapsed.value / logThreshold))

  const stepThreshold = props.durationMs / (STEPS.length + 0.6)
  stepDoneCount.value = Math.min(STEPS.length, Math.floor(elapsed.value / stepThreshold))

  if (elapsed.value < props.durationMs) {
    raf = requestAnimationFrame(tick)
  } else {
    visibleLogCount.value = logLines.value.length
    stepDoneCount.value = STEPS.length
    setTimeout(() => emit('complete'), 320)
  }
}

onMounted(() => {
  raf = requestAnimationFrame(tick)
})
onUnmounted(() => {
  if (raf) cancelAnimationFrame(raf)
})
</script>

<template>
  <div class="loader">
    <div class="loader__title mono">
      <span class="loader__spinner-dot"></span>
      SCANNING PLAYER PROFILE<span class="caret"></span>
    </div>

    <div class="loader__progress">
      <span class="loader__progress-label mono">FETCHING MATCH DATA</span>
      <div class="loader__bar mono">
        <span>{{ barText }}</span>
        <span class="loader__bar-pct">{{ progress }}%</span>
      </div>
    </div>

    <ul class="loader__steps">
      <li
        v-for="(step, i) in STEPS"
        :key="step"
        class="loader__step mono"
        :class="{
          'loader__step--done': i < stepDoneCount,
          'loader__step--active': i === stepDoneCount,
        }"
      >
        <span class="loader__step-mark">{{ i < stepDoneCount ? '✓' : '·' }}</span>
        {{ step }}
      </li>
    </ul>

    <div class="loader__log mono">
      <p v-for="(line, i) in logLines.slice(0, visibleLogCount)" :key="i" class="loader__log-line">
        {{ line }}
      </p>
      <p v-if="visibleLogCount < logLines.length" class="loader__log-line loader__log-line--pending">
        <span class="caret"></span>
      </p>
    </div>
  </div>
</template>

<style scoped>
.loader {
  display: flex;
  flex-direction: column;
  gap: 22px;
  padding: 32px;
  background: var(--bg-panel);
  border: 1px solid var(--line-subtle);
  border-radius: var(--radius-md);
  max-width: 520px;
  width: 100%;
}

.loader__title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  letter-spacing: 0.1em;
  color: var(--lime);
}

.loader__spinner-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--lime);
  box-shadow: 0 0 8px 1px var(--lime-glow);
  animation: pulse-dot 1s ease-in-out infinite;
}

.loader__progress {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.loader__progress-label {
  font-size: 10.5px;
  letter-spacing: 0.1em;
  color: var(--text-secondary);
}

.loader__bar {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  font-size: 13px;
  color: var(--lime);
  letter-spacing: -0.02em;
}

.loader__bar-pct {
  color: var(--cyan);
  font-size: 11.5px;
}

.loader__steps {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.loader__step {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  letter-spacing: 0.05em;
  color: var(--text-disabled);
  transition: color 0.2s var(--ease-out);
}

.loader__step-mark {
  width: 14px;
  color: inherit;
}

.loader__step--active {
  color: var(--cyan);
}

.loader__step--done {
  color: var(--text-secondary);
}
.loader__step--done .loader__step-mark {
  color: var(--lime);
}

.loader__log {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-top: 16px;
  border-top: 1px solid var(--line-faint);
  min-height: 108px;
}

.loader__log-line {
  font-size: 11.5px;
  color: var(--text-secondary);
}

.loader__log-line--pending {
  height: 1em;
}
</style>
