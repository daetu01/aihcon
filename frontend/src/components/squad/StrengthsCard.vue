<script setup lang="ts">
defineProps<{ strengths: string[] }>()
</script>

<template>
  <div class="notes panel">
    <span class="kicker">TEAM STRENGTHS</span>
    <ul class="notes__list">
      <li v-for="(s, i) in strengths" :key="i" class="notes__item">
        <span class="notes__mark notes__mark--pos mono">✓</span>
        <span>{{ s }}</span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.notes {
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notes__list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notes__item {
  display: flex;
  gap: 10px;
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-primary);
}

.notes__mark {
  flex-shrink: 0;
  font-weight: 700;
}

.notes__mark--pos {
  color: var(--lime);
}
</style>
