import { createApp } from 'vue'
import './assets/styles/tokens.css'
import './assets/styles/base.css'
import './assets/styles/utilities.css'
import App from './App.vue'
import router from './router'

createApp(App).use(router).mount('#app')
