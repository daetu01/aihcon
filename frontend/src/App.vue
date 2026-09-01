<script setup lang="ts">
import AppHeader from './components/AppHeader.vue'
</script>

<template>
  <AppHeader />
  <main class="app-main">
    <RouterView />
  </main>
</template>

<style scoped>
.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}
</style>
