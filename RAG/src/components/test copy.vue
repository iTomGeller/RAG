<template>
  <el-dropdown trigger="click" @command="handleCommand">
    <el-button :icon="currentMode === 'dark' ? Moon : Sunny" circle></el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="light">
          Light
        </el-dropdown-item>
        <el-dropdown-item command="dark">
          Dark
        </el-dropdown-item>
        <el-dropdown-item command="auto">
          System
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElButton, ElDropdown, ElDropdownMenu, ElDropdownItem } from 'element-plus';
import { Sunny, Moon } from '@element-plus/icons-vue';

// 使用 ref 管理当前的主题模式
const currentMode = ref('light'); // 默认白天模式

// 在组件挂载时初始化主题
onMounted(() => {
  const savedMode = localStorage.getItem('theme-mode');
  if (savedMode) {
    currentMode.value = savedMode;
  } else if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    // 如果系统偏好是暗色，则默认设置为跟随系统
    currentMode.value = 'auto';
  }
  applyTheme(currentMode.value);
});

// 应用主题的函数，根据模式操作 html 元素的 class
const applyTheme = (mode) => {
  const htmlElement = document.documentElement; // 获取 html 元素

  // 每次切换前先移除 dark-mode 类，确保正确切换
  htmlElement.classList.remove('dark-mode');

  if (mode === 'dark') {
    htmlElement.classList.add('dark-mode');
  } else if (mode === 'auto') {
    // 如果是跟随系统模式，检测系统偏好并添加 dark-mode 类
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      htmlElement.classList.add('dark-mode');
    }
  }
  // light 模式不需要添加特定的类，因为 Element Plus 默认就是亮色主题

  // 将当前主题模式保存到 localStorage，以便下次访问时记住用户的选择
  localStorage.setItem('theme-mode', mode);
};

// 下拉菜单命令处理函数
const handleCommand = (command) => {
  currentMode.value = command; // 更新当前模式
  applyTheme(command); // 应用新主题
};
</script>

<style>
/* --- 全局 CSS 变量定义 --- */
/* 这些变量定义通常放在 main.js 引入的全局 CSS 文件中，
   或者作为组件内非 scoped 样式 */

/* Element Plus 默认的白天模式变量（可以不写，或在这里覆盖默认值） */
:root {
  --el-color-primary: #409eff; /* 主题色 */
  --el-bg-color: #ffffff; /* 背景色 */
  --el-text-color-primary: #303133; /* 主要文字颜色 */
  /* ... 更多 Element Plus 白天模式变量 */
}

/* 黑夜模式变量 */
/* 当 html 元素有 dark-mode 类时，这些变量会覆盖默认值 */
html.dark-mode {
  --el-color-primary: #409eff; /* 你可以根据需求调整暗色模式的主题色 */
  --el-bg-color: #141414; /* 深色背景 */
  --el-bg-color-overlay: #1e1e1e; /* 弹出层背景 */
  --el-text-color-primary: #e6e6e6; /* 浅色文字 */
  --el-text-color-regular: #b0b0b0; /* 常规文字颜色 */
  --el-border-color: #4c4c4c; /* 边框颜色 */
  --el-border-color-light: #3a3a3a; /* 浅边框颜色 */
  --el-fill-color: #2b2b2b; /* 填充色 */
  --el-fill-color-light: #363636; /* 浅填充色 */
}

/* --- 平滑过渡效果 --- */
/* 为 html 和 body 元素添加过渡效果，让主题切换更平滑 */
html {
  transition: background-color 0.3s ease, color 0.3s ease;
}

body {
  background-color: var(--el-bg-color); /* 确保 body 背景色也基于变量 */
  color: var(--el-text-color-primary); /* 确保 body 文字颜色也基于变量 */
  transition: background-color 0.3s ease, color 0.3s ease;
}

</style>