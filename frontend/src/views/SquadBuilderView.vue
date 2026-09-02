<script setup lang="ts">
import { computed, nextTick, ref } from 'vue'
import { useRoute } from 'vue-router'
import { analyzeSquad } from '../api/squadApi'
import type { SquadAnalyzeResponse } from '../types/squad'
import SquadInputForm from '../components/squad/SquadInputForm.vue'
import SquadAnalyzingLoader from '../components/squad/SquadAnalyzingLoader.vue'
import CompatibilityScore from '../components/squad/CompatibilityScore.vue'
import TeamRadarChart from '../components/squad/TeamRadarChart.vue'
import PlayerProfileCard from '../components/squad/PlayerProfileCard.vue'
import StrengthsCard from '../components/squad/StrengthsCard.vue'
import WeaknessesCard from '../components/squad/WeaknessesCard.vue'

const route = useRoute()

const initialNicknames = computed(() => {
  const p1 = route.query.p1
  return typeof p1 === 'string' && p1.trim() ? [p1] : []
})

const loading = ref(false)
const errorMessage = ref('')
const result = ref<SquadAnalyzeResponse | null>(null)
const resultsRef = ref<HTMLElement | null>(null)

async function handleAnalyze(nicknames: string[]) {
  if (loading.value) return
  loading.value = true
  errorMessage.value = ''
  result.value = null

  try {
    result.value = await analyzeSquad(nicknames)
    await nextTick()
    resultsRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  } catch (err) {
    errorMessage.value = err instanceof Error ? err.message : '스쿼드 분석에 실패했습니다. 다시 시도해주세요.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="builder">
    <div class="builder__inner">
      <div class="builder__head">
        <span class="kicker">SQUAD.AI // AI SQUAD BUILDER</span>
        <h1 class="builder__title">AI Squad Builder</h1>
        <p class="builder__tagline mono">잘하는 사람보다, 잘 맞는 사람.</p>
        <p class="builder__desc">
          4명의 PUBG 닉네임을 입력하면 최근 전적 데이터를 분석해 각자의 플레이 성향을 분류하고,
          네 명이 함께 팀을 이뤘을 때의 스쿼드 궁합을 진단합니다.
        </p>
      </div>

      <SquadInputForm
        :loading="loading"
        :error-message="errorMessage"
        :initial-nicknames="initialNicknames"
        @analyze="handleAnalyze"
      />

      <SquadAnalyzingLoader v-if="loading" />

      <div v-if="result" ref="resultsRef" class="builder__results">
        <div class="builder__overview">
          <CompatibilityScore :score="result.compatibilityScore" />
          <TeamRadarChart :team-profile="result.teamProfile" />
        </div>

        <div class="builder__players">
          <PlayerProfileCard v-for="p in result.players" :key="p.nickname" :player="p" />
        </div>

        <div class="builder__notes">
          <StrengthsCard :strengths="result.strengths" />
          <WeaknessesCard :weaknesses="result.weaknesses" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.builder {
  flex: 1;
  padding: 40px 32px 100px;
}

.builder__inner {
  max-width: var(--shell-max);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.builder__head {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  max-width: 720px;
}

.builder__title {
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 700;
  margin-top: 4px;
}

.builder__tagline {
  font-size: 16px;
  font-weight: 700;
  color: var(--lime);
}

.builder__desc {
  font-size: 13.5px;
  line-height: 1.8;
  color: var(--text-secondary);
}

.builder__results {
  display: flex;
  flex-direction: column;
  gap: 24px;
  scroll-margin-top: calc(var(--header-h) + 24px);
}

.builder__overview {
  display: grid;
  grid-template-columns: 320px minmax(0, 1fr);
  gap: 24px;
  align-items: stretch;
}

.builder__players {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.builder__notes {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 1100px) {
  .builder__overview {
    grid-template-columns: 1fr;
  }
  .builder__players {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .builder__players {
    grid-template-columns: 1fr;
  }
  .builder__notes {
    grid-template-columns: 1fr;
  }
}
</style>
