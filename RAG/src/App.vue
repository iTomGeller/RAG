<script setup>
import { RouterView } from 'vue-router'
import { onMounted, ref } from 'vue'
import ThemeControlle from './components/utils/themeChange'

// 在组件挂载时初始化主题
onMounted(() => {
  ThemeControlle.changeTheme()
})
</script>
<template>
  <RouterView class="dark" />
</template>

<style></style>
