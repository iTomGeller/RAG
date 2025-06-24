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
</style>