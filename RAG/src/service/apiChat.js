import { fetchEventSource } from '@microsoft/fetch-event-source';
import baseURL from '@/config/baseURL';

export const startSSEChat = (memoryId, message, onMessage, onError) => {
  const token = localStorage.getItem('token'); // 从 localStorage 获取 token
  const controller = new AbortController();
  const messageEncoded = encodeURIComponent(message)

  fetchEventSource(`${baseURL}/chat?memoryId=${memoryId}&message=${messageEncoded}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain',
      'Authorization': `Bearer ${token}`, // 添加认证头
    },
    signal: controller.signal,
    onopen: async (response) => {
      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }
    },
    onmessage: (event) => {
      if (event.data) onMessage(event.data);
    },
    onerror: (err) => {
      onError(err);
      controller.abort(); // 中止请求
      throw err; // 阻止默认重试逻辑（可选）
    },
  });

  return () => controller.abort(); // 返回中止函数
};
