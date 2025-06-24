<script setup>
import Main from '../components/Main/Main.vue'
import Sidebar from '@/components/Siderbar/Sidebar.vue';
</script>

<template>
  <div class="home">
   <Sidebar />
    <Main />
  </div>
</template>

<style scoped>
.home {
  display: flex; 
  height: 100vh; 
  width: 100%; 
}

</style>