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
axiosClient.interceptors.response.use(
  response => {
    const { code, msg, data } = response.data;

    if (code === 0) {
      ElNotification({
        message: msg || '业务异常',
        type: "error",
      })
      // 业务异常，抛出错误
      return Promise.reject(new Error(msg || '业务异常'));
    } else if (code === 1) {
      // 正常情况，返回数据
      return data;
    } else {
      // 其他 code 的处理（可选）
      return Promise.reject(new Error('未知的业务状态'));
    }
  },
  error => {
    console.error('响应错误:', error)
    // 对响应错误做点什么
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，重定向到登录页
          ElNotification({
            message: "登录已过期，请重新登录",
            type: "error",
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
      }
    }
    return Promise.reject(error)
  }
)

export default api;
