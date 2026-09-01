<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  synergyScore: number
  reasons: string[]
}>()

const dialStyle = computed(() => ({
  background: `conic-gradient(var(--lime) ${props.synergyScore * 3.6}deg, var(--line-subtle) ${props.synergyScore * 3.6}deg)`,
}))

const rating = computed(() => {
  if (props.synergyScore >= 90) return 'OPTIMAL MATCH'
  if (props.synergyScore >= 78) return 'STRONG MATCH'
  if (props.synergyScore >= 60) return 'VIABLE MATCH'
  return 'LOW COMPATIBILITY'
})
</script>

<template>
  <div class="synergy panel">
    <div class="brackets"><span class="bracket-tl"></span><span class="bracket-br"></span></div>

    <div class="synergy__head">
      <span class="kicker">SQUAD SYNERGY</span>
      <span class="badge badge--lime">{{ rating }}</span>
    </div>

    <div class="synergy__dial-wrap">
      <div class="synergy__dial" :style="dialStyle">
        <div class="synergy__dial-inner">
          <span class="synergy__score mono">{{ synergyScore }}</span>
          <span class="synergy__percent mono">%</span>
        </div>
      </div>
    </div>

    <div class="synergy__reasons">
      <span class="kicker kicker--cyan synergy__reasons-title">WHY THIS SQUAD?</span>
      <ul class="synergy__list">
        <li v-for="(reason, i) in reasons" :key="i" class="synergy__item">
          <span class="synergy__item-mark mono">›</span>
          <span class="synergy__item-text">{{ reason }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.synergy {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.synergy__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.synergy__dial-wrap {
  display: flex;
  justify-content: center;
  padding: 8px 0 4px;
}

.synergy__dial {
  width: 168px;
  height: 168px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 18px rgba(167, 255, 63, 0.18));
}

.synergy__dial-inner {
  width: 134px;
  height: 134px;
  border-radius: 50%;
  background: var(--bg-void);
  border: 1px solid var(--line-subtle);
  display: flex;
  align-items: baseline;
  justify-content: center;
}

.synergy__score {
  font-size: 44px;
  font-weight: 700;
  color: var(--lime);
  text-shadow: 0 0 18px var(--lime-glow);
}

.synergy__percent {
  font-size: 18px;
  color: var(--lime-dim);
  margin-left: 2px;
}

.synergy__reasons {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-top: 18px;
  border-top: 1px solid var(--line-faint);
}

.synergy__reasons-title {
  display: block;
}

.synergy__list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.synergy__item {
  display: flex;
  gap: 8px;
  font-size: 12.5px;
  line-height: 1.6;
  color: var(--text-secondary);
}

.synergy__item-mark {
  color: var(--lime);
  flex-shrink: 0;
}

.synergy__item-text {
  color: var(--text-primary);
}
</style>
