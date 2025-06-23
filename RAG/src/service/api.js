// src/utils/api.js
import axios from 'axios';
import baseURL from '@/config/baseURL';
// axios.defaults.withCredentials = true;  // 只有后端设置了 AllowCredentials 才用

const api = axios.create({
  baseURL: `${baseURL}/api`,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器：自动带上 token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('userToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  else {
    console.error('token not found')
  }
  return config;
}, (error) => Promise.reject(error));

export default api;
