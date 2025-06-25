import axios from 'axios';
import baseURL from '@/config/baseURL';

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

export default api;
