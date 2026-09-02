<script setup lang="ts">
defineProps<{ weaknesses: string[] }>()
</script>

<template>
  <div class="notes panel">
    <span class="kicker" style="color: var(--amber)">RISK ANALYSIS</span>
    <ul class="notes__list">
      <li v-for="(w, i) in weaknesses" :key="i" class="notes__item">
        <span class="notes__mark notes__mark--neg mono">!</span>
        <span>{{ w }}</span>
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

.notes__mark--neg {
  color: var(--amber);
}
</style>
