<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { fetchSquadRecommendation } from '../api/pubgApi'
import type { SquadRecommendation } from '../types/pubg'
import { usePlayerSession } from '../composables/usePlayerSession'
import SquadSlot from '../components/SquadSlot.vue'
import SynergyPanel from '../components/SynergyPanel.vue'

const props = defineProps<{ nickname: string }>()
const router = useRouter()
const { setLastNickname } = usePlayerSession()

const loading = ref(true)
const recommendation = ref<SquadRecommendation | null>(null)
const variant = ref(0)
const deployed = ref(false)

async function load(nickname: string, v: number) {
  loading.value = true
  deployed.value = false
  setLastNickname(nickname)
  recommendation.value = await fetchSquadRecommendation(nickname, v)
  loading.value = false
}

onMounted(() => load(props.nickname, variant.value))
watch(() => props.nickname, (n) => {
  variant.value = 0
  load(n, 0)
})

function findAnother() {
  variant.value += 1
  load(props.nickname, variant.value)
}

function deploy() {
  deployed.value = true
}

function goAnalysis() {
  router.push({ name: 'analysis', params: { nickname: props.nickname } })
}
</script>

<template>
  <div class="builder">
    <div class="builder__inner">
      <div class="builder__head">
        <div>
          <span class="kicker">AI SQUAD BUILDER</span>
          <h1 class="builder__title">4인 스쿼드 편성</h1>
        </div>
        <button class="btn btn-ghost" @click="goAnalysis">‹ BACK TO REPORT</button>
      </div>

      <div v-if="loading" class="builder__loading mono">
        <span class="status-dot status-dot--live"></span>
        MATCHING COMPATIBLE PLAYERS<span class="caret"></span>
      </div>

      <div v-else-if="recommendation" class="builder__grid">
        <div class="builder__slots">
          <div class="builder__net" aria-hidden="true"></div>
          <SquadSlot
            v-for="(m, i) in recommendation.members"
            :key="m.nickname"
            :index="i + 1"
            :member="m"
          />
        </div>

        <div class="builder__side">
          <SynergyPanel :synergy-score="recommendation.synergyScore" :reasons="recommendation.reasons" />

          <div class="builder__deploy panel">
            <p v-if="deployed" class="builder__deploy-msg mono">
              <span class="status-dot status-dot--live"></span>
              SQUAD DEPLOYED — INVITES SENT TO 3 PLAYERS
            </p>
            <div class="builder__deploy-actions">
              <button class="btn btn-primary" :disabled="deployed" @click="deploy">DEPLOY SQUAD</button>
              <button class="btn btn-ghost" @click="findAnother">FIND ANOTHER SQUAD</button>
            </div>
          </div>
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
  gap: 28px;
}

.builder__head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.builder__title {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  margin-top: 6px;
}

.builder__loading {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 60px 0;
  justify-content: center;
  color: var(--cyan);
  letter-spacing: 0.08em;
  font-size: 13px;
}

.builder__grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  gap: 28px;
  align-items: start;
}

.builder__slots {
  position: relative;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.builder__net {
  position: absolute;
  inset: 0;
  pointer-events: none;
  background-image:
    linear-gradient(var(--line-faint) 1px, transparent 1px),
    linear-gradient(90deg, var(--line-faint) 1px, transparent 1px);
  background-size: 50% 50%;
  background-position: center;
  opacity: 0.6;
}

.builder__side {
  display: flex;
  flex-direction: column;
  gap: 20px;
  position: sticky;
  top: calc(var(--header-h) + 24px);
}

.builder__deploy {
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.builder__deploy-msg {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  letter-spacing: 0.05em;
  color: var(--lime);
}

.builder__deploy-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

@media (max-width: 1100px) {
  .builder__grid {
    grid-template-columns: 1fr;
  }
  .builder__side {
    position: static;
  }
}

@media (max-width: 640px) {
  .builder__slots {
    grid-template-columns: 1fr;
  }
}
</style>
