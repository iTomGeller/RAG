<template>
  <Sidebar @update:extended="handleSidebarToggle" />
  <div class="container" :style="{ marginLeft: sidebarWidth + 'px' }">
  <router-view />
  </div>
</template>

<script setup>
import Sidebar from '@/components/Siderbar/Sidebar.vue'
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const sidebarWidth = ref(75)
const handleSidebarToggle = (isExtended) => {
  sidebarWidth.value = isExtended ? 200 : 75
}

// 在组件挂载时，如果是根路径，自动跳转到默认页面
onMounted(() => {
  if (route.path === '/home') {
    console.log('Hello')
    router.push('/home/chat')
  }
})
</script>

<style scoped>
.container {
  transition: margin-left 0.3s ease; /* 平滑过渡效果 */
  display: flex;
  height: 100vh;
  width: 100%;
}
</style>
