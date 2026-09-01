<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    label: string
    value: number
    max?: number
    accent?: 'lime' | 'cyan'
    segments?: number
  }>(),
  {
    max: 100,
    accent: 'lime',
    segments: 24,
  },
)

const ratio = computed(() => Math.min(1, Math.max(0, props.value / props.max)))
const filledSegments = computed(() => Math.round(ratio.value * props.segments))
</script>

<template>
  <div class="gauge">
    <div class="gauge__head">
      <span class="gauge__label mono">{{ label }}</span>
      <span class="gauge__value mono" :class="`gauge__value--${accent}`">{{ value }}</span>
    </div>
    <div class="gauge__track" :class="`gauge__track--${accent}`">
      <span
        v-for="i in segments"
        :key="i"
        class="gauge__seg"
        :class="{ 'gauge__seg--on': i <= filledSegments }"
      />
    </div>
  </div>
</template>

<style scoped>
.gauge {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.gauge__head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}

.gauge__label {
  font-size: 10.5px;
  letter-spacing: 0.1em;
  color: var(--text-secondary);
  text-transform: uppercase;
}

.gauge__value {
  font-size: 13px;
  font-weight: 700;
}

.gauge__value--lime {
  color: var(--lime);
}
.gauge__value--cyan {
  color: var(--cyan);
}

.gauge__track {
  display: grid;
  grid-template-columns: repeat(v-bind('props.segments'), 1fr);
  gap: 2px;
  height: 8px;
}

.gauge__seg {
  background: var(--bg-inset);
  border: 1px solid var(--line-faint);
  border-radius: 1px;
}

.gauge__track--lime .gauge__seg--on {
  background: var(--lime);
  border-color: var(--lime);
  box-shadow: 0 0 5px -1px var(--lime-glow);
}

.gauge__track--cyan .gauge__seg--on {
  background: var(--cyan);
  border-color: var(--cyan);
  box-shadow: 0 0 5px -1px rgba(69, 224, 230, 0.5);
}
</style>
