<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePlayerSession } from '../composables/usePlayerSession'

withDefaults(
  defineProps<{
    variant?: 'hero' | 'compact'
    placeholder?: string
  }>(),
  {
    variant: 'hero',
    placeholder: 'PUBG PLAYER NAME',
  },
)

const router = useRouter()
const { setLastNickname } = usePlayerSession()
const nickname = ref('')
const error = ref('')

function submit() {
  const value = nickname.value.trim()
  if (!value) {
    error.value = 'ENTER A PLAYER NAME TO BEGIN SCAN'
    return
  }
  error.value = ''
  setLastNickname(value)
  router.push({ name: 'analysis', params: { nickname: value } })
}
</script>

<template>
  <form class="search" :class="`search--${variant}`" @submit.prevent="submit">
    <div class="search__field">
      <span class="search__bracket mono">[</span>
      <input
        v-model="nickname"
        class="search__input mono"
        type="text"
        :placeholder="placeholder"
        autocomplete="off"
        spellcheck="false"
        @input="error = ''"
      />
      <span class="search__bracket mono">]</span>
    </div>
    <button type="submit" class="btn btn-primary search__submit">
      <span>ANALYZE PLAYER</span>
      <span aria-hidden="true">›</span>
    </button>
    <p v-if="error" class="search__error mono">// {{ error }}</p>
  </form>
</template>

<style scoped>
.search {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

.search--hero {
  max-width: 440px;
}

.search--compact {
  max-width: 320px;
}

.search__field {
  display: flex;
  align-items: center;
  height: 52px;
  background: var(--bg-inset);
  border: 1px solid var(--line-strong);
  border-radius: var(--radius-sm);
  padding: 0 4px;
  transition: border-color 0.15s var(--ease-out), box-shadow 0.15s var(--ease-out);
}

.search__field:focus-within {
  border-color: var(--lime);
  box-shadow: 0 0 0 1px rgba(167, 255, 63, 0.2), 0 0 18px -6px var(--lime-glow);
}

.search__bracket {
  color: var(--text-tertiary);
  font-size: 15px;
  padding: 0 8px;
}

.search__input {
  flex: 1;
  height: 100%;
  font-size: 13.5px;
  letter-spacing: 0.05em;
  color: var(--text-primary);
}

.search__input::placeholder {
  color: var(--text-disabled);
}

.search__submit {
  width: 100%;
}

.search__error {
  font-size: 11px;
  color: var(--red);
  letter-spacing: 0.03em;
}
</style>
