<script setup lang="ts">
import { computed, reactive, watch } from 'vue'

const props = withDefaults(
  defineProps<{
    loading?: boolean
    errorMessage?: string
    initialNicknames?: string[]
  }>(),
  {
    loading: false,
    errorMessage: '',
    initialNicknames: () => [],
  },
)

const emit = defineEmits<{ analyze: [nicknames: string[]] }>()

const nicknames = reactive(['', '', '', ''])

watch(
  () => props.initialNicknames,
  (list) => {
    list.forEach((n, i) => {
      if (i < 4 && n) nicknames[i] = n
    })
  },
  { immediate: true },
)

const trimmed = computed(() => nicknames.map((n) => n.trim()))
const hasEmpty = computed(() => trimmed.value.some((n) => !n))
const hasDuplicate = computed(() => {
  const filled = trimmed.value.filter(Boolean).map((n) => n.toLowerCase())
  return new Set(filled).size !== filled.length
})
const dirty = computed(() => trimmed.value.some((n) => n))
const canSubmit = computed(() => !hasEmpty.value && !hasDuplicate.value && !props.loading)

const validationMessage = computed(() => {
  if (!dirty.value) return ''
  if (hasDuplicate.value) return '중복된 닉네임이 있습니다.'
  if (hasEmpty.value) return '플레이어 닉네임 4명을 모두 입력해주세요.'
  return ''
})

function submit() {
  if (!canSubmit.value) return
  emit('analyze', trimmed.value)
}
</script>

<template>
  <form class="input-form panel" @submit.prevent="submit">
    <div class="input-form__grid">
      <div v-for="(_, i) in nicknames" :key="i" class="input-form__field">
        <label class="input-form__label mono">PLAYER {{ i + 1 }}</label>
        <div class="input-form__box">
          <input
            v-model="nicknames[i]"
            class="input-form__input mono"
            type="text"
            placeholder="PUBG NICKNAME"
            autocomplete="off"
            spellcheck="false"
            :disabled="loading"
          />
        </div>
      </div>
    </div>

    <div class="input-form__footer">
      <p v-if="validationMessage" class="input-form__hint input-form__hint--warn mono">
        // {{ validationMessage }}
      </p>
      <p v-else-if="errorMessage" class="input-form__hint input-form__hint--error mono">
        // {{ errorMessage }}
      </p>
      <span v-else class="input-form__hint input-form__hint--placeholder"></span>

      <button type="submit" class="btn btn-primary input-form__submit" :disabled="!canSubmit">
        <span>{{ loading ? 'ANALYZING...' : 'ANALYZE SQUAD' }}</span>
        <span aria-hidden="true">›</span>
      </button>
    </div>
  </form>
</template>

<style scoped>
.input-form {
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.input-form__grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.input-form__field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-form__label {
  font-size: 10.5px;
  letter-spacing: 0.14em;
  color: var(--text-tertiary);
}

.input-form__box {
  display: flex;
  align-items: center;
  height: 46px;
  background: var(--bg-inset);
  border: 1px solid var(--line-strong);
  border-radius: var(--radius-sm);
  padding: 0 12px;
  transition: border-color 0.15s var(--ease-out), box-shadow 0.15s var(--ease-out);
}

.input-form__box:focus-within {
  border-color: var(--lime);
  box-shadow: 0 0 0 1px rgba(167, 255, 63, 0.2), 0 0 18px -6px var(--lime-glow);
}

.input-form__input {
  flex: 1;
  height: 100%;
  font-size: 13px;
  letter-spacing: 0.03em;
  color: var(--text-primary);
}

.input-form__input::placeholder {
  color: var(--text-disabled);
}

.input-form__input:disabled {
  opacity: 0.5;
}

.input-form__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding-top: 4px;
  flex-wrap: wrap;
}

.input-form__hint {
  font-size: 11.5px;
  letter-spacing: 0.02em;
}

.input-form__hint--warn {
  color: var(--amber);
}

.input-form__hint--error {
  color: var(--red);
}

.input-form__hint--placeholder {
  height: 1em;
}

.input-form__submit {
  margin-left: auto;
  min-width: 200px;
}

@media (max-width: 900px) {
  .input-form__grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 560px) {
  .input-form__grid {
    grid-template-columns: 1fr;
  }
  .input-form__submit {
    width: 100%;
  }
}
</style>
