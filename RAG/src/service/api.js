import axios from 'axios';
import baseURL from '@/config/baseURL';
import router from '@/router';

const api = axios.create({
  baseURL: `${baseURL}`,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器：自动带上 token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  else {
    console.error('token not found')
  }
  return config;
}, (error) => Promise.reject(error));


// 响应拦截器
api.interceptors.response.use((response) => {
  console.log(response)
  const { data, status, statusText } = response

  if (status != 200) {
    switch (status) {
      case 401:
        // 未授权，重定向到登录页
        ElNotification({
          message: '登录已过期，请重新登录',
          type: 'error',
        })
        router.push('/login')
        break
      case 403:
        // 权限不足
        console.error('没有权限访问该资源')
        break
      case 404:
        // 资源不存在
        console.error('请求的资源不存在')
        break
      default:
        console.error('服务器错误')
        return Promise.reject(error)
    }
  }

  return response
})

export default api
