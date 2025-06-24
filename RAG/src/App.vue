<script setup>
import GeminiProvider from './components/context/GeminiProvider.vue';
import { RouterView } from 'vue-router'
import Sidebar from './components/Siderbar/Sidebar.vue';

</script>

<template>
  <GeminiProvider>

      <RouterView />

  </GeminiProvider>
</template>

<style scoped>
.container 
{
  display: flex;
  height: 100vh;
  width: 100%;
}
</style>