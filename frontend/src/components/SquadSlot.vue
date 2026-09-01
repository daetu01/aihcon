<script setup lang="ts">
import type { SquadMember } from '../types/pubg'
import PlayerCard from './PlayerCard.vue'

defineProps<{
  index: number
  member: SquadMember
}>()
</script>

<template>
  <div class="slot">
    <div class="slot__node mono">
      <span class="slot__dot" :class="{ 'slot__dot--you': member.isYou }"></span>
      {{ member.isYou ? 'SLOT 01 // YOU' : `SLOT 0${index} // MEMBER` }}
    </div>
    <PlayerCard :member="member" />
  </div>
</template>

<style scoped>
.slot {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.slot__node {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 10px;
  letter-spacing: 0.14em;
  color: var(--text-tertiary);
}

.slot__dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--cyan);
  box-shadow: 0 0 6px 1px rgba(69, 224, 230, 0.5);
}

.slot__dot--you {
  background: var(--lime);
  box-shadow: 0 0 6px 1px var(--lime-glow);
}
</style>
