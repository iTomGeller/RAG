<script setup>
// import GeminiProvider from './components/context/GeminiProvider.vue';
import { RouterView } from 'vue-router'
import { onMounted, ref } from 'vue';
import ThemeControlle from './components/utils/themeChange';
import ChatProvider from './components/context/ChatProvider.vue';

// 在组件挂载时初始化主题
onMounted(() => {
  ThemeControlle.changeTheme();
});

</script>
<template>
  <ChatProvider>

    <RouterView class="dark" />

  </ChatProvider>
</template>

<style></style>