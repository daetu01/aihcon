import { ref } from 'vue'

const STORAGE_KEY = 'squadai.lastPlayer'

const lastNickname = ref<string>(sessionStorage.getItem(STORAGE_KEY) ?? '')

export function usePlayerSession() {
  function setLastNickname(nickname: string) {
    lastNickname.value = nickname
    sessionStorage.setItem(STORAGE_KEY, nickname)
  }

  return { lastNickname, setLastNickname }
}
