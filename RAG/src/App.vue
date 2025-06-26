<script setup>
import GeminiProvider from './components/context/GeminiProvider.vue';
import { RouterView } from 'vue-router'

</script>
<template>
  <GeminiProvider>

    <RouterView />

  </GeminiProvider>
</template>

<style >

</style>