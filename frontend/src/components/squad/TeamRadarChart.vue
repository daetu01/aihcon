<script setup lang="ts">
import { computed } from 'vue'
import type { TeamProfile } from '../../types/squad'
import RadarChart from '../RadarChart.vue'

const props = defineProps<{ teamProfile: TeamProfile }>()

const points = computed(() => [
  { label: 'AGGRESSION', value: props.teamProfile.aggression },
  { label: 'COMBAT', value: props.teamProfile.combat },
  { label: 'MOBILITY', value: props.teamProfile.mobility },
  { label: 'SUPPORT', value: props.teamProfile.support },
  { label: 'SURVIVAL', value: props.teamProfile.survival },
])
</script>

<template>
  <div class="team-radar panel">
    <span class="kicker">TEAM PROFILE</span>
    <div class="team-radar__body">
      <RadarChart :points="points" :size="248" />
    </div>
  </div>
</template>

<style scoped>
.team-radar {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.team-radar__body {
  display: flex;
  justify-content: center;
  padding: 16px 0 8px;
}
</style>
