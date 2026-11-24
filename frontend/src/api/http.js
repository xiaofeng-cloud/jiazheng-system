import axios from 'axios'

// 所有业务请求统一经过 /api，开发环境由 Vite 转发到 Spring Boot。
const http = axios.create({ baseURL: '/api', timeout: 10000 })
http.interceptors.request.use((config) => {
  const token = localStorage.getItem('jiazheng-token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})
export default http
