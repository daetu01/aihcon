<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'

const STEPS = [
  'CONNECTING TO PUBG DATA NODE',
  'ANALYZING PLAYER TELEMETRY',
  'CLASSIFYING PLAY STYLE',
  'BUILDING SQUAD PROFILE',
  'CALCULATING COMPATIBILITY',
]

const STEP_MS = 1500

const activeIndex = ref(0)
let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
  timer = setInterval(() => {
    activeIndex.value = (activeIndex.value + 1) % STEPS.length
  }, STEP_MS)
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<template>
  <div class="loader panel">
    <div class="loader__title mono">
      <span class="loader__dot"></span>
      ANALYZING SQUAD<span class="caret"></span>
    </div>

    <div class="loader__bar"><span class="loader__bar-fill"></span></div>

    <ul class="loader__steps">
      <li
        v-for="(step, i) in STEPS"
        :key="step"
        class="loader__step mono"
        :class="{ 'loader__step--active': i === activeIndex, 'loader__step--done': i < activeIndex }"
      >
        <span class="loader__step-mark">{{ i < activeIndex ? '✓' : i === activeIndex ? '›' : '·' }}</span>
        {{ step }}
      </li>
    </ul>

    <p class="loader__hint mono">
      전적 데이터를 여러 차례 조회하는 중입니다. 최대 수십 초가 소요될 수 있습니다.
    </p>
  </div>
</template>

<style scoped>
.loader {
  padding: 28px 32px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.loader__title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  letter-spacing: 0.1em;
  color: var(--lime);
}

.loader__dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--lime);
  box-shadow: 0 0 8px 1px var(--lime-glow);
  animation: pulse-dot 1s ease-in-out infinite;
}

.loader__bar {
  position: relative;
  height: 3px;
  overflow: hidden;
  background: var(--bg-inset);
  border-radius: 2px;
}

.loader__bar-fill {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 36%;
  background: linear-gradient(90deg, transparent, var(--lime), transparent);
  animation: loader-sweep 1.4s ease-in-out infinite;
}

@keyframes loader-sweep {
  0% {
    left: -36%;
  }
  100% {
    left: 100%;
  }
}

.loader__steps {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.loader__step {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11.5px;
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

.loader__hint {
  font-size: 11px;
  color: var(--text-tertiary);
  padding-top: 14px;
  border-top: 1px solid var(--line-faint);
}
</style>
