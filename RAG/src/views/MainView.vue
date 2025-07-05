<template>
  <Sidebar @update:extended="handleSidebarToggle" @open-feedback="isDrawerVisible = true" />
  <FeedbackDrawer v-model:visible="isDrawerVisible"/>
  <div class="container">
    <div class="router-container" :style="{marginLeft: sidebarWidth + 'px'}">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import FeedbackDrawer from '@/components/Siderbar/FeedbackDrawer.vue'
import Sidebar from '@/components/Siderbar/Sidebar.vue'
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const sidebarWidth = ref(75)
const handleSidebarToggle = (isExtended) => {
  sidebarWidth.value = isExtended ? 250 : 75
}

const isDrawerVisible = ref(false);

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
  height: 100vh;
  width: 100%;
}
.router-container {
  transition: margin-left 0.3s ease; /* 平滑过渡效果 */
}
</style>
