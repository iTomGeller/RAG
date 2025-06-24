<template>
  <div class="settings-container">
    <div class="settings-content">
      <el-card class="theme-card">
        <div class="settings-header">
          <h2>主题设置</h2>
        </div>
        
        <div class="theme-options">
          <el-radio-group v-model="theme" @change="handleThemeChange">
            <el-radio label="light">浅色主题</el-radio>
            <el-radio label="dark">深色主题</el-radio>
          </el-radio-group>
        </div>

        <!-- 主题预览 -->
        <div class="theme-preview">
          <div class="preview-container" :class="theme">
            <div class="preview-header">
              <div class="preview-nav">
                <span>导航栏</span>
              </div>
            </div>
            <div class="preview-content">
              <div class="preview-card">
                <p>卡片内容</p>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';

// 主题状态，默认为浅色
const theme = ref('light');

// 切换主题时更新文档属性和本地存储
const handleThemeChange = (val: string) => {
  document.documentElement.setAttribute('data-theme', val);
  localStorage.setItem('theme', val);
};

// 组件挂载后检查本地存储的主题设置
onMounted(() => {
  const savedTheme = localStorage.getItem('theme') || 'light';
  theme.value = savedTheme;
  document.documentElement.setAttribute('data-theme', savedTheme);
});
</script>

<style scoped>
.settings-container {
  display: flex;
  justify-content: center;
  padding: 24px;
  min-height: 100vh;
  transition: background-color 0.3s ease;
}

.settings-content {
  width: 960px;
  max-width: 100%;
}

.theme-card {
  margin-top: 16px;
}

.settings-header h2 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.theme-options {
  margin-bottom: 20px;
  padding: 16px 0;
}

.theme-preview {
  margin-top: 24px;
}

.preview-container {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.preview-container.light {
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
}

.preview-container.dark {
  background-color: #141414;
  border: 1px solid #434343;
}

.preview-header {
  padding: 12px 16px;
  transition: background-color 0.3s ease;
}

.preview-container.light .preview-header {
  background-color: #ffffff;
  border-bottom: 1px solid #dcdfe6;
}

.preview-container.dark .preview-header {
  background-color: #1f1f1f;
  border-bottom: 1px solid #434343;
}

.preview-nav {
  font-size: 14px;
  transition: color 0.3s ease;
}

.preview-container.light .preview-nav {
  color: #303133;
}

.preview-container.dark .preview-nav {
  color: #e5eaf3;
}

.preview-content {
  padding: 16px;
}

.preview-card {
  padding: 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.preview-container.light .preview-card {
  background-color: #ffffff;
  border: 1px solid #ebeef5;
  color: #606266;
}

.preview-container.dark .preview-card {
  background-color: #1f1f1f;
  border: 1px solid #434343;
  color: #c9cdd4;
}
</style>

<!-- 全局主题样式 -->
<style>
[data-theme='light'] {
  --el-color-primary: #409eff;
  --el-bg-color: #ffffff;
  --el-text-color-primary: #303133;
  --el-border-color-light: #dcdfe6;
}

[data-theme='dark'] {
  --el-color-primary: #409eff;
  --el-bg-color: #141414;
  --el-text-color-primary: #e5eaf3;
  --el-border-color-light: #434343;
}

body {
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
  transition: background-color 0.3s ease, color 0.3s ease;
}
</style>