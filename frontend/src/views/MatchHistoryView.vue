<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { fetchPlayerMatches } from '../api/pubgApi'
import type { MatchRecord } from '../types/pubg'
import { usePlayerSession } from '../composables/usePlayerSession'

const props = defineProps<{ nickname: string }>()
const { setLastNickname } = usePlayerSession()

const loading = ref(true)
const matches = ref<MatchRecord[]>([])
const expandedId = ref<string | null>(null)

async function load(nickname: string) {
  loading.value = true
  setLastNickname(nickname)
  matches.value = await fetchPlayerMatches(nickname)
  loading.value = false
}

onMounted(() => load(props.nickname))
watch(() => props.nickname, (n) => load(n))

function toggle(id: string) {
  expandedId.value = expandedId.value === id ? null : id
}

function formatDate(iso: string): string {
  const d = new Date(iso)
  return d.toLocaleDateString('en-CA') + ' ' + d.toLocaleTimeString('en-GB', { hour12: false }).slice(0, 5)
}

function rankTier(rank: number): 'gold' | 'mid' | 'low' {
  if (rank <= 3) return 'gold'
  if (rank <= 10) return 'mid'
  return 'low'
}

const avgDamage = computed(() => {
  if (!matches.value.length) return 0
  return Math.round(matches.value.reduce((s, m) => s + m.damage, 0) / matches.value.length)
})
</script>

<template>
  <div class="matches">
    <div class="matches__inner">
      <div class="matches__head">
        <div>
          <span class="kicker">MATCH HISTORY // {{ nickname }}</span>
          <h1 class="matches__title">DATA SOURCE // PUBG MATCH HISTORY</h1>
        </div>
      </div>

      <div class="matches__meta mono">
        <div class="matches__meta-item">
          <span class="kicker kicker--dim">MATCHES SCANNED</span>
          <span class="matches__meta-value">{{ matches.length || '—' }}</span>
        </div>
        <div class="matches__meta-item">
          <span class="kicker kicker--dim">AVG DAMAGE</span>
          <span class="matches__meta-value">{{ avgDamage || '—' }}</span>
        </div>
        <div class="matches__meta-item">
          <span class="kicker kicker--dim">LAST SYNC</span>
          <span class="matches__meta-value matches__meta-value--cyan">2 MIN AGO</span>
        </div>
      </div>

      <div v-if="loading" class="matches__loading mono">
        <span class="status-dot status-dot--live"></span>
        SYNCING MATCH RECORDS<span class="caret"></span>
      </div>

      <div v-else class="panel matches__table-wrap">
        <table class="matches__table">
          <thead>
            <tr>
              <th>MATCH</th>
              <th>MODE</th>
              <th>RANK</th>
              <th>KILLS</th>
              <th>DAMAGE</th>
              <th>SURVIVAL</th>
              <th>DATE</th>
            </tr>
          </thead>
          <tbody>
            <template v-for="(m, i) in matches" :key="m.id">
              <tr class="matches__row" @click="toggle(m.id)">
                <td class="mono">#{{ String(matches.length - i).padStart(2, '0') }}</td>
                <td><span class="badge">{{ m.mode }}</span></td>
                <td>
                  <span class="matches__rank mono" :class="`matches__rank--${rankTier(m.rank)}`">
                    #{{ m.rank }}
                  </span>
                </td>
                <td class="mono">{{ m.kills }}</td>
                <td class="mono">{{ m.damage }}</td>
                <td class="mono">{{ m.survivalTimeMin.toFixed(1) }}m</td>
                <td class="mono matches__date">{{ formatDate(m.date) }}</td>
              </tr>
              <tr v-if="expandedId === m.id" class="matches__detail-row">
                <td colspan="7">
                  <div class="matches__detail mono">
                    <span>MAP // {{ m.map }}</span>
                    <span>PLAYERS // {{ m.totalPlayers }}</span>
                    <span>MATCH ID // {{ m.id }}</span>
                  </div>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.matches {
  flex: 1;
  padding: 40px 32px 100px;
}

.matches__inner {
  max-width: var(--shell-max);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.matches__title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  margin-top: 6px;
  letter-spacing: 0.02em;
}

.matches__meta {
  display: flex;
  gap: 40px;
  padding: 16px 20px;
  background: var(--bg-panel);
  border: 1px solid var(--line-subtle);
  border-radius: var(--radius-md);
}

.matches__meta-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.matches__meta-value {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

.matches__meta-value--cyan {
  color: var(--cyan);
}

.matches__loading {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 50px 0;
  justify-content: center;
  color: var(--cyan);
  font-size: 13px;
  letter-spacing: 0.08em;
}

.matches__table-wrap {
  overflow-x: auto;
}

.matches__table {
  width: 100%;
  border-collapse: collapse;
  min-width: 640px;
}

.matches__table thead th {
  text-align: left;
  font-family: var(--font-mono);
  font-size: 10px;
  letter-spacing: 0.1em;
  color: var(--text-tertiary);
  padding: 14px 20px;
  border-bottom: 1px solid var(--line-subtle);
  white-space: nowrap;
}

.matches__row {
  cursor: pointer;
  transition: background 0.12s var(--ease-out);
}

.matches__row:hover {
  background: rgba(167, 255, 63, 0.04);
}

.matches__row td {
  padding: 12px 20px;
  font-size: 12.5px;
  color: var(--text-primary);
  border-bottom: 1px solid var(--line-faint);
  white-space: nowrap;
}

.matches__rank--gold {
  color: var(--lime);
  font-weight: 700;
}
.matches__rank--mid {
  color: var(--cyan);
}
.matches__rank--low {
  color: var(--text-secondary);
}

.matches__date {
  color: var(--text-tertiary);
}

.matches__detail-row td {
  padding: 0;
  border-bottom: 1px solid var(--line-faint);
}

.matches__detail {
  display: flex;
  gap: 28px;
  padding: 12px 20px;
  background: var(--bg-inset);
  font-size: 11px;
  color: var(--text-secondary);
}

@media (max-width: 640px) {
  .matches__meta {
    flex-wrap: wrap;
    gap: 20px;
  }
}
</style>
