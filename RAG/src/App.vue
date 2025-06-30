<script setup>
import GeminiProvider from './components/context/GeminiProvider.vue';
import { RouterView } from 'vue-router'
import { onMounted, ref } from 'vue';

// 在组件挂载时初始化主题
onMounted(() => {
  const body = document.body;

  const isDarkMode = ref(false);
  // 1. 尝试从 localStorage 读取用户偏好
  const savedTheme = localStorage.getItem('theme-preference');
  if (savedTheme === 'dark') {
    isDarkMode.value = true;
    body.classList.add('dark');
    body.style.setProperty('background-color', 'var(--el-bg-color-page)')
    body.style.setProperty('color', 'white')
    body.style.setProperty('transition', 'background-color 0.3s ease, color 0.3s ease')
    console.log('First using dark');
  } else if (savedTheme === 'light') {
    isDarkMode.value = false;
    body.classList.remove('dark');
    body.style.setProperty('background-color', '');
    body.style.setProperty('color', '');
    body.style.setProperty('transition', '');
    console.log('First using light');
  } else {
    // 2. 如果没有保存的偏好，检测系统偏好
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDarkMode.value = true;
    }
    // 将用户偏好保存到 localStorage
    console.log('Applied theme:', isDarkMode.value ? 'dark' : 'light');
  }
});

</script>
<template>
  <GeminiProvider>

    <RouterView class="dark" />

  </GeminiProvider>
</template>

<style></style>