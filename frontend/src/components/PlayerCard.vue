<script setup lang="ts">
import type { SquadMember } from '../types/pubg'
import { ROLE_LABEL } from '../types/pubg'
import StatGauge from './StatGauge.vue'

defineProps<{
  member: SquadMember
}>()
</script>

<template>
  <div class="player-card" :class="{ 'player-card--you': member.isYou }">
    <div class="player-card__top">
      <div>
        <span class="player-card__tag mono">{{ member.isYou ? 'YOU' : 'SQUAD MEMBER' }}</span>
        <h3 class="player-card__name">{{ member.nickname }}</h3>
      </div>
      <span class="badge" :class="member.isYou ? 'badge--lime' : 'badge--cyan'">
        {{ ROLE_LABEL[member.role] }}
      </span>
    </div>

    <div class="player-card__gauges">
      <StatGauge label="AGGRESSION" :value="member.scores.aggression" :segments="16" />
      <StatGauge label="SURVIVAL" :value="member.scores.survival" :segments="16" accent="cyan" />
      <StatGauge label="TEAMPLAY" :value="member.scores.teamplay" :segments="16" />
    </div>

    <div v-if="!member.isYou" class="player-card__match">
      <span class="mono">MATCH</span>
      <span class="player-card__match-value mono">{{ member.matchPercent }}%</span>
    </div>
  </div>
</template>

<style scoped>
.player-card {
  position: relative;
  background: var(--bg-panel);
  border: 1px solid var(--line-subtle);
  border-radius: var(--radius-md);
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  transition: border-color 0.2s var(--ease-out), transform 0.2s var(--ease-out);
}

.player-card--you {
  border-color: rgba(167, 255, 63, 0.35);
  box-shadow: 0 0 0 1px rgba(167, 255, 63, 0.08), 0 0 22px -10px var(--lime-glow);
}

.player-card__top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.player-card__tag {
  display: block;
  font-size: 9.5px;
  letter-spacing: 0.14em;
  color: var(--text-tertiary);
  margin-bottom: 4px;
}

.player-card__name {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: 0.01em;
}

.player-card__gauges {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.player-card__match {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  padding-top: 12px;
  border-top: 1px solid var(--line-faint);
  font-size: 10.5px;
  letter-spacing: 0.1em;
  color: var(--text-tertiary);
}

.player-card__match-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--cyan);
}
</style>
