<template>
  <div class="theme-toggle">
    <el-button @click="toggleTheme" link>
      <el-icon :size="20">
        <template v-if="isDarkMode">
          <Sunny />
        </template>
        <template v-else>
          <Moon />
        </template>
      </el-icon>
      </el-button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { ElButton, ElIcon } from 'element-plus';
import { Sunny, Moon } from '@element-plus/icons-vue'; // 导入 Element Plus 图标

// 定义一个响应式变量来存储当前是否是暗色模式
const isDarkMode = ref(false);

// 监听 isDarkMode 的变化，并应用主题
watch(isDarkMode, (newVal) => {
  applyTheme(newVal);
});

// 在组件挂载时初始化主题
onMounted(() => {
  // 1. 尝试从 localStorage 读取用户偏好
  const savedTheme = localStorage.getItem('theme-preference');
  if (savedTheme === 'dark') {
    isDarkMode.value = true;
    console.log('First using dark');
  } else if (savedTheme === 'light') {
    isDarkMode.value = false;
    console.log('First using light');
  } else {
    // 2. 如果没有保存的偏好，检测系统偏好
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDarkMode.value = true;
    }
  }
  applyTheme(isDarkMode.value);
});

const applyTheme = (dark) => {
  const body = document.body;
  if (dark) {
    body.classList.add('dark');
  } else {
    body.classList.remove('dark');
  }
  // 将用户偏好保存到 localStorage
  console.log('Applied theme:', dark ? 'dark' : 'light');
  localStorage.setItem('theme-preference', dark ? 'dark' : 'light');
};

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value;
};
</script>

<style scoped>
.theme-toggle {
  display: inline-flex;
  align-items: center;
}

.el-button.is-link {
  color: var(--font-normal); 
}

.el-button.is-link:hover {
  color: var(--primary-color); 
}
</style>