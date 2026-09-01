import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0 }
  },
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
    },
    {
      path: '/analysis/:nickname',
      name: 'analysis',
      component: () => import('../views/PlayerAnalysisView.vue'),
      props: true,
    },
    {
      path: '/squad/:nickname',
      name: 'squad',
      component: () => import('../views/SquadBuilderView.vue'),
      props: true,
    },
    {
      path: '/matches/:nickname',
      name: 'matches',
      component: () => import('../views/MatchHistoryView.vue'),
      props: true,
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
})

export default router
