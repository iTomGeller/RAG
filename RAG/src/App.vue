<script setup>
import GeminiProvider from './components/context/GeminiProvider.vue';
import { RouterView } from 'vue-router'
</script>

<template>
  <!-- 这里使用context技术进行上下文共享 -->
  <GeminiProvider>  
    <RouterView />
  </GeminiProvider>
</template>

<style scoped></style>