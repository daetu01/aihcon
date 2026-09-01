<script setup lang="ts">
import PlayerSearch from '../components/PlayerSearch.vue'
import RadarChart from '../components/RadarChart.vue'
import StatGauge from '../components/StatGauge.vue'

const demoScores = [
  { label: 'AGGRESSION', value: 78 },
  { label: 'SURVIVAL', value: 86 },
  { label: 'TEAMPLAY', value: 91 },
  { label: 'MOBILITY', value: 64 },
]

const steps = [
  {
    n: '01',
    title: 'SCAN',
    ko: '최근 경기 데이터 수집',
    en: 'Pull recent match history straight from the source.',
  },
  {
    n: '02',
    title: 'PROFILE',
    ko: 'AI 플레이 스타일 분석',
    en: 'Score aggression, survival, teamplay and mobility.',
  },
  {
    n: '03',
    title: 'SYNC',
    ko: '궁합이 맞는 플레이어 탐색',
    en: 'Cross-reference profiles to find complementary players.',
  },
  {
    n: '04',
    title: 'DEPLOY',
    ko: '최적의 4인 스쿼드 구성',
    en: 'Assemble the four-player squad with the highest synergy.',
  },
]
</script>

<template>
  <div class="home">
    <section class="hero">
      <div class="hero__grid">
        <div class="hero__copy">
          <span class="kicker hero__eyebrow">AI SQUAD BUILDER // PUBG INTELLIGENCE</span>
          <h1 class="hero__title">
            SQUAD<span class="hero__title-dot">.</span>AI
          </h1>
          <p class="hero__tagline mono">STOP SEARCHING.<br />START SYNCING.</p>
          <p class="hero__desc">
            최근 경기 데이터를 분석해 당신의 플레이 스타일을 파악하고,<br />
            가장 잘 맞는 스쿼드를 자동으로 구성합니다.
          </p>
          <PlayerSearch variant="hero" />
        </div>

        <div class="hero__hud">
          <div class="hud panel panel--glow">
            <div class="brackets"><span class="bracket-tl"></span><span class="bracket-br"></span></div>
            <div class="hud__head">
              <span class="kicker">PLAYER // DAEAN123</span>
              <span class="badge badge--lime">
                <span class="status-dot status-dot--live"></span>
                &nbsp;LIVE PREVIEW
              </span>
            </div>

            <div class="hud__body">
              <RadarChart :points="demoScores" :size="200" />

              <div class="hud__gauges">
                <StatGauge
                  v-for="s in demoScores"
                  :key="s.label"
                  :label="s.label"
                  :value="s.value"
                  :segments="14"
                />
              </div>
            </div>

            <div class="hud__footer">
              <div class="hud__role">
                <span class="kicker kicker--dim">ROLE</span>
                <span class="hud__role-value">TACTICAL SUPPORT</span>
              </div>
              <div class="hud__status">
                <span class="kicker kicker--dim">STATUS</span>
                <span class="hud__status-value mono">
                  <span class="status-dot"></span>
                  PLAYER PROFILE READY
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="how">
      <div class="how__inner">
        <span class="kicker how__eyebrow">HOW IT WORKS</span>
        <div class="how__steps">
          <div v-for="(s, i) in steps" :key="s.n" class="how__step">
            <div class="how__step-head">
              <span class="how__step-n mono">{{ s.n }}</span>
              <span class="how__step-title">{{ s.title }}</span>
            </div>
            <p class="how__step-ko">{{ s.ko }}</p>
            <p class="how__step-en mono">{{ s.en }}</p>
            <span v-if="i < steps.length - 1" class="how__connector" aria-hidden="true"></span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home {
  flex: 1;
}

.hero {
  padding: 88px 32px 64px;
}

.hero__grid {
  max-width: var(--shell-max);
  margin: 0 auto;
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 460px);
  gap: 72px;
  align-items: center;
}

.hero__copy {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 22px;
}

.hero__eyebrow {
  font-size: 11px;
}

.hero__title {
  font-family: var(--font-display);
  font-size: 64px;
  font-weight: 700;
  letter-spacing: 0.01em;
  line-height: 1;
  color: var(--text-primary);
}

.hero__title-dot {
  color: var(--lime);
  text-shadow: 0 0 22px var(--lime-glow);
}

.hero__tagline {
  font-size: 30px;
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -0.01em;
  color: var(--text-primary);
}

.hero__desc {
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-secondary);
  max-width: 480px;
}

.hero__hud {
  display: flex;
  justify-content: center;
}

.hud {
  padding: 22px;
  width: 100%;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.hud__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.hud__body {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.hud__gauges {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hud__footer {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid var(--line-faint);
}

.hud__role,
.hud__status {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.hud__role-value {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: 15px;
  color: var(--lime);
}

.hud__status-value {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11.5px;
  color: var(--text-secondary);
}

.how {
  padding: 40px 32px 100px;
  border-top: 1px solid var(--line-faint);
}

.how__inner {
  max-width: var(--shell-max);
  margin: 0 auto;
}

.how__eyebrow {
  display: block;
  margin-bottom: 28px;
}

.how__steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 28px;
}

.how__step {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-right: 20px;
}

.how__step-head {
  display: flex;
  align-items: baseline;
  gap: 10px;
}

.how__step-n {
  font-size: 13px;
  color: var(--lime);
  font-weight: 700;
}

.how__step-title {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.03em;
}

.how__step-ko {
  font-size: 13px;
  color: var(--text-secondary);
}

.how__step-en {
  font-size: 10.5px;
  color: var(--text-tertiary);
  line-height: 1.5;
}

.how__connector {
  display: none;
}

@media (min-width: 901px) {
  .how__connector {
    display: block;
    position: absolute;
    top: 8px;
    right: -14px;
    width: 28px;
    height: 1px;
    background: linear-gradient(90deg, var(--line-strong), transparent);
  }
}

@media (max-width: 1100px) {
  .hero__grid {
    grid-template-columns: 1fr;
  }
  .hero__hud {
    order: -1;
  }
}

@media (max-width: 900px) {
  .how__steps {
    grid-template-columns: repeat(2, 1fr);
  }
  .hero__title {
    font-size: 46px;
  }
}
</style>
