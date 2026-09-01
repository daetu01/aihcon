<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { fetchPlayerProfile } from '../api/pubgApi'
import type { PlayerProfile } from '../types/pubg'
import { ROLE_LABEL } from '../types/pubg'
import { usePlayerSession } from '../composables/usePlayerSession'
import TerminalLoader from '../components/TerminalLoader.vue'
import StatGauge from '../components/StatGauge.vue'
import RadarChart from '../components/RadarChart.vue'

const props = defineProps<{ nickname: string }>()
const router = useRouter()
const { setLastNickname } = usePlayerSession()

const loading = ref(true)
const scanning = ref(true)
const profile = ref<PlayerProfile | null>(null)

const scoreEntries = computed(() => {
  if (!profile.value) return []
  const s = profile.value.scores
  return [
    { label: 'AGGRESSION', value: s.aggression },
    { label: 'SURVIVAL', value: s.survival },
    { label: 'TEAM PLAY', value: s.teamplay },
    { label: 'MOBILITY', value: s.mobility },
    { label: 'COMBAT INITIATIVE', value: s.combatInitiative },
  ]
})

const radarPoints = computed(() => {
  if (!profile.value) return []
  const s = profile.value.scores
  return [
    { label: 'AGG', value: s.aggression },
    { label: 'SURV', value: s.survival },
    { label: 'TEAM', value: s.teamplay },
    { label: 'MOB', value: s.mobility },
    { label: 'INIT', value: s.combatInitiative },
  ]
})

async function load(nickname: string) {
  loading.value = true
  scanning.value = true
  profile.value = null
  setLastNickname(nickname)
  profile.value = await fetchPlayerProfile(nickname)
  loading.value = false
}

function onScanComplete() {
  scanning.value = false
}

onMounted(() => load(props.nickname))
watch(() => props.nickname, (n) => load(n))

function goSquad() {
  router.push({ name: 'squad', params: { nickname: props.nickname } })
}
function goMatches() {
  router.push({ name: 'matches', params: { nickname: props.nickname } })
}
</script>

<template>
  <div class="report">
    <div class="report__inner">
      <div class="report__head">
        <div>
          <span class="kicker">PLAYER INTELLIGENCE REPORT</span>
          <h1 class="report__title">{{ nickname }}</h1>
        </div>
        <div class="report__actions">
          <button class="btn btn-ghost" @click="goMatches">MATCH HISTORY</button>
          <button class="btn btn-primary" @click="goSquad">BUILD SQUAD</button>
        </div>
      </div>

      <div v-if="loading || scanning" class="report__loading">
        <TerminalLoader :nickname="nickname" @complete="onScanComplete" />
      </div>

      <div v-else-if="profile" class="report__grid">
        <div class="report__col">
          <section class="panel card">
            <span class="kicker">PLAYER SUMMARY</span>
            <div class="summary-grid">
              <div class="summary-item">
                <span class="summary-item__label mono">NICKNAME</span>
                <span class="summary-item__value">{{ profile.summary.nickname }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-item__label mono">MATCHES ANALYZED</span>
                <span class="summary-item__value mono">{{ profile.summary.matchesAnalyzed }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-item__label mono">AVG DAMAGE</span>
                <span class="summary-item__value mono">{{ profile.summary.avgDamage }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-item__label mono">K/D</span>
                <span class="summary-item__value mono">{{ profile.summary.kd.toFixed(2) }}</span>
              </div>
              <div class="summary-item">
                <span class="summary-item__label mono">AVG SURVIVAL TIME</span>
                <span class="summary-item__value mono">{{ profile.summary.avgSurvivalTimeMin.toFixed(1) }}m</span>
              </div>
              <div class="summary-item">
                <span class="summary-item__label mono">TOP 10 RATE</span>
                <span class="summary-item__value mono">{{ profile.summary.top10Rate }}%</span>
              </div>
            </div>
          </section>

          <section class="panel card">
            <span class="kicker">PLAY STYLE SCORES</span>
            <div class="report__scores">
              <div class="report__gauges">
                <StatGauge
                  v-for="s in scoreEntries"
                  :key="s.label"
                  :label="s.label"
                  :value="s.value"
                  :segments="20"
                />
              </div>
              <RadarChart :points="radarPoints" :size="200" />
            </div>
          </section>
        </div>

        <div class="report__col">
          <section class="panel panel--glow card">
            <div class="brackets"><span class="bracket-tl"></span><span class="bracket-br"></span></div>
            <span class="kicker">AI CLASSIFICATION</span>
            <h2 class="report__role">{{ ROLE_LABEL[profile.analysis.role] }}</h2>
            <p class="report__desc">{{ profile.analysis.description }}</p>

            <div class="trait-grid">
              <div class="trait-block">
                <span class="kicker kicker--cyan">STRENGTH</span>
                <ul class="trait-list">
                  <li v-for="t in profile.analysis.strengths" :key="t" class="trait-list__item trait-list__item--pos">
                    <span class="mono">+</span>{{ t }}
                  </li>
                </ul>
              </div>
              <div class="trait-block">
                <span class="kicker" style="color: var(--red)">WEAKNESS</span>
                <ul class="trait-list">
                  <li v-for="t in profile.analysis.weaknesses" :key="t" class="trait-list__item trait-list__item--neg">
                    <span class="mono">−</span>{{ t }}
                  </li>
                </ul>
              </div>
            </div>
          </section>

          <section class="panel card">
            <span class="kicker">RECOMMENDED TEAMMATES</span>
            <div class="teammates">
              <div v-for="role in profile.analysis.recommendedRoles" :key="role" class="teammate-tile">
                <span class="teammate-tile__role">{{ ROLE_LABEL[role] }}</span>
                <span class="teammate-tile__note mono">COMPLEMENTS YOUR PROFILE</span>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.report {
  flex: 1;
  padding: 40px 32px 100px;
}

.report__inner {
  max-width: var(--shell-max);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.report__head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.report__title {
  font-family: var(--font-display);
  font-size: 34px;
  font-weight: 700;
  margin-top: 6px;
}

.report__actions {
  display: flex;
  gap: 10px;
}

.report__loading {
  display: flex;
  justify-content: center;
  padding: 60px 0;
}

.report__grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 24px;
  align-items: start;
}

.report__col {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px 24px;
}

.summary-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--line-faint);
}

.summary-item__label {
  font-size: 10px;
  letter-spacing: 0.1em;
  color: var(--text-tertiary);
}

.summary-item__value {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

.report__scores {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 22px;
}

.report__gauges {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.report__role {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: var(--lime);
  text-shadow: 0 0 16px var(--lime-glow);
}

.report__desc {
  font-size: 13.5px;
  line-height: 1.85;
  color: var(--text-secondary);
}

.trait-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--line-faint);
}

.trait-block {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.trait-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.trait-list__item {
  display: flex;
  gap: 8px;
  font-size: 12.5px;
  color: var(--text-secondary);
}

.trait-list__item--pos span {
  color: var(--lime);
}
.trait-list__item--neg span {
  color: var(--red);
}

.teammates {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.teammate-tile {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px 14px;
  background: var(--bg-inset);
  border: 1px solid var(--line-subtle);
  border-radius: var(--radius-sm);
  transition: border-color 0.15s var(--ease-out);
}

.teammate-tile:hover {
  border-color: var(--cyan);
}

.teammate-tile__role {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: 13px;
  color: var(--text-primary);
}

.teammate-tile__note {
  font-size: 9.5px;
  letter-spacing: 0.06em;
  color: var(--text-tertiary);
}

@media (max-width: 1100px) {
  .report__grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .summary-grid,
  .trait-grid,
  .teammates {
    grid-template-columns: 1fr;
  }
}
</style>
