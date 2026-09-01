<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerSession } from '../composables/usePlayerSession'

const route = useRoute()
const { lastNickname } = usePlayerSession()

const now = ref(new Date())
let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
  timer = setInterval(() => {
    now.value = new Date()
  }, 1000)
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const clock = computed(() =>
  now.value.toLocaleTimeString('en-GB', { hour12: false, timeZone: 'UTC' }),
)

const navItems = computed(() => {
  const nick = lastNickname.value
  return [
    { label: 'HOME', to: '/', enabled: true },
    { label: 'PLAYER ANALYSIS', to: nick ? `/analysis/${nick}` : '/', enabled: !!nick },
    { label: 'SQUAD BUILDER', to: nick ? `/squad/${nick}` : '/', enabled: !!nick },
    { label: 'MATCH HISTORY', to: nick ? `/matches/${nick}` : '/', enabled: !!nick },
  ]
})

function isActive(to: string): boolean {
  return route.path === to || (to !== '/' && route.path.startsWith(to.split('/').slice(0, 2).join('/')))
}
</script>

<template>
  <header class="app-header">
    <div class="app-header__inner">
      <RouterLink to="/" class="brand">
        <span class="brand__mark">▚</span>
        <span class="brand__text">
          <span class="brand__name">SQUAD<span class="brand__dot">.</span>AI</span>
          <span class="brand__sub mono">TACTICAL SQUAD INTELLIGENCE</span>
        </span>
      </RouterLink>

      <nav class="nav">
        <RouterLink
          v-for="item in navItems"
          :key="item.label"
          :to="item.to"
          class="nav__link"
          :class="{ 'nav__link--active': isActive(item.to), 'nav__link--disabled': !item.enabled }"
        >
          {{ item.label }}
        </RouterLink>
      </nav>

      <div class="status mono">
        <span class="status-dot status-dot--live"></span>
        <span class="status__label">SYSTEM ONLINE</span>
        <span class="status__sep">/</span>
        <span class="status__clock">{{ clock }} UTC</span>
      </div>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 50;
  height: var(--header-h);
  background: rgba(10, 14, 19, 0.86);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--line-subtle);
}

.app-header__inner {
  max-width: var(--shell-max);
  height: 100%;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  align-items: center;
  gap: 40px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand__mark {
  color: var(--lime);
  font-size: 20px;
  line-height: 1;
  text-shadow: 0 0 12px var(--lime-glow);
}

.brand__text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.brand__name {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: 17px;
  letter-spacing: 0.04em;
  color: var(--text-primary);
}

.brand__dot {
  color: var(--lime);
}

.brand__sub {
  font-size: 9px;
  letter-spacing: 0.14em;
  color: var(--text-tertiary);
}

.nav {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
}

.nav__link {
  font-family: var(--font-mono);
  font-size: 11.5px;
  font-weight: 500;
  letter-spacing: 0.08em;
  padding: 8px 14px;
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  border: 1px solid transparent;
  transition: color 0.15s var(--ease-out), border-color 0.15s var(--ease-out), background 0.15s var(--ease-out);
}

.nav__link:hover {
  color: var(--text-primary);
}

.nav__link--active {
  color: var(--lime);
  background: var(--lime-soft);
  border-color: rgba(167, 255, 63, 0.28);
}

.nav__link--disabled {
  color: var(--text-disabled);
  pointer-events: none;
}

.status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  color: var(--text-secondary);
  white-space: nowrap;
}

.status__label {
  color: var(--text-secondary);
}

.status__sep {
  color: var(--text-tertiary);
}

.status__clock {
  color: var(--cyan);
}

@media (max-width: 900px) {
  .nav {
    display: none;
  }
  .app-header__inner {
    gap: 16px;
  }
}
</style>
