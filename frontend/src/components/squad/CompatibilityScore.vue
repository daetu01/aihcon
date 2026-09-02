<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ score: number }>()

const dialStyle = computed(() => ({
  background: `conic-gradient(var(--lime) ${props.score * 3.6}deg, var(--line-subtle) ${props.score * 3.6}deg)`,
}))

/** Front-end-only presentation band. The raw compatibilityScore from the backend is never altered. */
const status = computed(() => {
  if (props.score >= 80) return { label: 'EXCELLENT', badge: 'badge--lime' }
  if (props.score >= 60) return { label: 'GOOD SYNERGY', badge: 'badge--cyan' }
  if (props.score >= 40) return { label: 'FAIR', badge: 'badge--amber' }
  return { label: 'LOW SYNERGY', badge: 'badge--red' }
})
</script>

<template>
  <div class="score panel panel--glow">
    <div class="brackets"><span class="bracket-tl"></span><span class="bracket-br"></span></div>
    <span class="kicker">SQUAD COMPATIBILITY</span>

    <div class="score__dial-wrap">
      <div class="score__dial" :style="dialStyle">
        <div class="score__dial-inner">
          <span class="score__value mono">{{ score }}</span>
        </div>
      </div>
    </div>

    <span class="badge score__status" :class="status.badge">{{ status.label }}</span>
  </div>
</template>

<style scoped>
.score {
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.score__dial-wrap {
  display: flex;
  justify-content: center;
  padding: 6px 0;
}

.score__dial {
  width: 176px;
  height: 176px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 18px rgba(167, 255, 63, 0.18));
}

.score__dial-inner {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: var(--bg-void);
  border: 1px solid var(--line-subtle);
  display: flex;
  align-items: center;
  justify-content: center;
}

.score__value {
  font-size: 52px;
  font-weight: 700;
  color: var(--lime);
  text-shadow: 0 0 18px var(--lime-glow);
}

.score__status {
  height: 26px;
  font-size: 11px;
}
</style>
