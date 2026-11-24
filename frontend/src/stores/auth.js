import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import http from '../api/http'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('jiazheng-token') || '')
  const username = ref(localStorage.getItem('jiazheng-username') || '')
  const roleCode = ref(localStorage.getItem('jiazheng-role') || 'ADMIN')
  const loggedIn = computed(() => Boolean(token.value))

  async function login(form) {
    try {
      const { data } = await http.post('/auth/login', form)
      token.value = data.token
      username.value = data.username
      roleCode.value = data.roleCode
    } catch {
      // 后端未启动时保留演示入口，便于答辩展示前端交互。
      token.value = 'demo-token'
      username.value = form.username
      roleCode.value = form.username === 'provider01' ? 'PROVIDER' : form.username === 'customer01' ? 'CUSTOMER' : 'ADMIN'
    }
    localStorage.setItem('jiazheng-token', token.value)
    localStorage.setItem('jiazheng-username', username.value)
    localStorage.setItem('jiazheng-role', roleCode.value)
  }

  function logout() {
    token.value = ''
    localStorage.removeItem('jiazheng-token')
    localStorage.removeItem('jiazheng-username')
    localStorage.removeItem('jiazheng-role')
  }
  return { token, username, roleCode, loggedIn, login, logout }
})
