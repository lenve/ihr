import { ref } from 'vue'
import { defineStore } from 'pinia'
import { loginApi, type UserInfoVO } from '@/api/auth'

const TOKEN_KEY = 'ihr_token'
const USER_KEY = 'ihr_user'

function readUserInfo(): UserInfoVO | null {
  try {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? (JSON.parse(raw) as UserInfoVO) : null
  } catch {
    return null
  }
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) || '')
  const userInfo = ref<UserInfoVO | null>(readUserInfo())

  async function login(username: string, password: string) {
    const data = await loginApi({ username, password })
    token.value = data.token
    userInfo.value = data.user
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify(data.user))
  }

  /** 仅清除本地登录态（登出为 JWT 无状态方案，客户端删 token 即可） */
  function logoutLocal() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  function displayName(): string {
    return userInfo.value?.nickname || userInfo.value?.username || ''
  }

  return { token, userInfo, login, logoutLocal, displayName }
})
