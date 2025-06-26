import axios from 'axios';
import baseURL from '@/config/baseURL';
import router from '@/router';
import { ElNotification } from 'element-plus';
import { useRouter } from 'vue-router';

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
  // console.log("hello"); // 调试信息
  console.log("响应成功:", response);

  // 这里的 response.status 应该是 2xx 范围内的成功状态码
  // 例如 200, 201, 204 等
  // 通常，你可以在这里处理成功的业务逻辑，例如解析数据
  const { data } = response;

  // if (data && data.code !== 200) { // 假设你的后端有一个业务状态码
  //   // 即使 HTTP 状态码是 200，业务逻辑上也可能表示失败
  //   ElNotification({
  //     message: data.message || '业务操作失败',
  //     type: 'warning',
  //   });
  //   // 可以选择在这里返回 Promise.reject 来将业务错误传播到调用方
  //   // return Promise.reject(new Error(data.message || '业务操作失败'));
  // }

  return response; // 确保返回 response
}, (error) => {
  // 响应失败，例如收到 4xx, 5xx 状态码或网络错误
  console.error("响应失败:", error);

  const { response } = error; // 错误对象中包含服务器响应信息

  if (response) {
    const { status, data } = response;

    switch (status) {
      case 400:
        ElNotification({
          message: '请求参数错误',
        });
        console.error("请求参数错误:", data);
        break;
      case 401:
        // 未授权，重定向到登录页
        ElNotification({
          message: '登录已过期或未授权，请重新登录',
        });
        const router = useRouter();
        localStorage.removeItem('token'); // 清除过期 token
        router.push('/login');
        break;
      case 403:
        // 权限不足
        ElNotification({
          message: '权限不足，无法访问该资源',
        });
        console.error('没有权限访问该资源');
        break;
      case 404:
        // 资源不存在
        ElNotification({
          message: '请求的资源不存在',
        });
        console.error('请求的资源不存在');
        break;
      case 500:
        // 服务器内部错误
        ElNotification({
          message: '服务器内部错误，请稍后再试',
        });
        console.error('服务器内部错误', error);

        break;
      default:
        // 其他未知错误
        ElNotification({
          message: `请求失败: ${status || ''} ${data.message || error.message || '未知错误'}`,
        });
        console.error('服务器错误或其他请求失败:', error);
        break;
    }
  } else if (error.request) {
    // 请求已发出但没有收到响应
    // `error.request` 在浏览器中是 XMLHttpRequest 实例
    // 在 node.js 中是 http.ClientRequest 实例
    ElNotification({
      message: '服务器无响应，请稍后再试',
      type: 'error',
    });
    console.error('请求已发送但未收到响应:', error.request);
  } else {
    // 在设置请求的时候发生了错误
    ElNotification({
      message: '请求配置错误',
      type: 'error',
    });
    console.error('请求设置错误:', error.message);
  }

  // 必须返回 Promise.reject(error) 来将错误传播给调用方
  // 否则，调用方在 .catch() 中将无法捕获到错误
  return Promise.reject(error);
});

export default api 

