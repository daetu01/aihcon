<script setup lang="ts">
import type { PlayerInfo } from '../../types/squad'
import { playStyleBadgeClass, playStyleLabel } from '../../types/squad'
import StatGauge from '../StatGauge.vue'

defineProps<{ player: PlayerInfo }>()
</script>

<template>
  <div class="player-card">
    <div class="player-card__top">
      <h3 class="player-card__name">{{ player.nickname }}</h3>
      <span class="badge" :class="playStyleBadgeClass(player.playStyle)">
        {{ playStyleLabel(player.playStyle) }}
      </span>
    </div>

    <div class="player-card__gauges">
      <StatGauge label="AGGRESSION" :value="player.aggression" accent="lime" :segments="14" />
      <StatGauge label="COMBAT" :value="player.combat" accent="cyan" :segments="14" />
      <StatGauge label="MOBILITY" :value="player.mobility" accent="lime" :segments="14" />
      <StatGauge label="SUPPORT" :value="player.support" accent="cyan" :segments="14" />
      <StatGauge label="SURVIVAL" :value="player.survival" accent="lime" :segments="14" />
    </div>
  </div>
</template>

<style scoped>
.player-card {
  background: var(--bg-panel);
  border: 1px solid var(--line-subtle);
  border-radius: var(--radius-md);
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  transition: border-color 0.2s var(--ease-out);
}

.player-card:hover {
  border-color: var(--line-strong);
}

.player-card__top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}

.player-card__name {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: 0.01em;
  word-break: break-word;
}

.player-card__gauges {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
