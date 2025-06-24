<template>
  <div class="container">
    <Sidebar />
    <router-view></router-view>
  </div>
</template>

<script setup>
import Sidebar from '@/components/Siderbar/Sidebar.vue';
import { onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();


// 在组件挂载时，如果是根路径，自动跳转到默认页面
onMounted(() => {
  if (route.path === '/home') {
   console.log("Hello")
    router.push('/home/chat');
  }
});

</script>

<style scoped>
.container {
  display: flex;
  height: 100vh;
  width: 100%;
}
</style>