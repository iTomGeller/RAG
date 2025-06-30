<template>
  <div class="settings-container">
    <div class="settings-content">
      <el-card class="theme-card">
        <div class="settings-header">
          <h2>{{ t('settings.theme') }}</h2>
        </div>

        <TranslateButton />
        <!-- 主题切换 -->
        <div class="theme-toggle">
          <div class="theme-switch">
            <span>{{ t('settings.light') }}</span>
            <!-- 动态属性绑定 -->
            <el-switch
              v-model="isDarkTheme"
              @change="toggleTheme"
              :active-text="t('settings.dark')"
              inactive-color="#f5f7fa"
              active-color="#1a1a1a"
            />
          </div>
        </div>

        <!-- 主题预览 -->
        <div class="theme-preview">
          <h3>{{ t('settings.preview') }}</h3>
          <div class="preview-container" :class="{ dark: isDarkTheme }">
            <div class="preview-header">
              <div class="preview-nav">
                <span>{{ t('settings.nav') }}</span>
                <el-button size="small" icon="refresh" @click="resetTheme">{{ t('settings.reset') }}</el-button>
              </div>
            </div>
            <div class="preview-content">
              <div class="preview-card">
                <p>{{ t('settings.content') }}</p>
                <el-button type="primary">{{ t('settings.mainBtn') }}</el-button>
                <el-button>{{ t('settings.secondaryBtn') }}</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 高级设置 -->
        <div class="advanced-settings">
          <h3>{{ t('settings.advancedSettings') }}</h3>
          <el-form label-position="top">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="t('settings.mainColor')">
                  <el-color-picker v-model="primaryColor" @change="updatePrimaryColor" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="t('settings.bgTransparency')">
                  <el-slider v-model="bgOpacity" :min="0" :max="100" @change="updateBackground" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { Refresh } from '@element-plus/icons-vue'
import TranslateButton from '@/components/Main/TranslateButton.vue';

//全局翻译
const { t } = useI18n()
// 主题状态
const isDarkTheme = ref(false)
const primaryColor = ref('#409eff')
const bgOpacity = ref(100)

// 切换主题模式
const toggleTheme = (val: boolean) => {
  updateTheme(val)
  localStorage.setItem('theme', val ? 'dark' : 'light')
}

// 更新主题
const updateTheme = (isDark: boolean) => {
  const root = document.documentElement

  // 设置基础主题变量
  if (isDark) {
    root.style.setProperty('--el-bg-color', `#141414${getOpacityValue()}`)
    root.style.setProperty('--el-bg-secondary', '#1f1f1f')
    root.style.setProperty('--el-text-color-primary', '#e5eaf3')
    root.style.setProperty('--el-border-color-light', '#434343')
  } else {
    root.style.setProperty('--el-bg-color', `#ffffff${getOpacityValue()}`)
    root.style.setProperty('--el-bg-secondary', '#f5f7fa')
    root.style.setProperty('--el-text-color-primary', '#303133')
    root.style.setProperty('--el-border-color-light', '#dcdfe6')
  }

  // 应用主色调
  root.style.setProperty('--el-color-primary', primaryColor.value)
}

// 获取背景透明度值
const getOpacityValue = () => {
  const opacity = bgOpacity.value / 100
  // 转换为十六进制表示（两位）
  const hex = Math.round(opacity * 255).toString(16)
  return hex.length === 1 ? '0' + hex : hex
}

// 更新主色调
const updatePrimaryColor = (color: string) => {
  primaryColor.value = color
  updateTheme(isDarkTheme.value)
}

// 更新背景设置
const updateBackground = () => {
  updateTheme(isDarkTheme.value)
}

// 重置主题
const resetTheme = () => {
  isDarkTheme.value = false
  primaryColor.value = '#409eff'
  bgOpacity.value = 100
  updateTheme(false)
  localStorage.removeItem('theme')
}

// 组件挂载后检查本地存储的主题设置
onMounted(() => {
  const savedTheme = localStorage.getItem('theme') || 'light'
  isDarkTheme.value = savedTheme === 'dark'
  updateTheme(isDarkTheme.value)
})
</script>

<style scoped>
.settings-container {
  display: flex;
  justify-content: center;
  padding: 24px;
  min-height: 100vh;
  background-color: var(--el-bg-color);
  transition: background-color 0.3s ease;
}

.settings-content {
  width: 960px;
  max-width: 100%;
}

.theme-card {
  margin-top: 16px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.settings-header h2 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  padding: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.theme-toggle {
  padding: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.theme-switch {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.theme-preview {
  padding: 16px;
}

.theme-preview h3 {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 500;
}

.preview-container {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid var(--el-border-color-light);
}

.preview-header {
  padding: 12px 16px;
  background-color: var(--el-bg-secondary);
  transition: background-color 0.3s ease;
}

.preview-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-content {
  padding: 16px;
  background-color: var(--el-bg-color);
  transition: background-color 0.3s ease;
}

.preview-card {
  padding: 16px;
  border-radius: 4px;
  background-color: var(--el-bg-secondary);
  border: 1px solid var(--el-border-color-light);
  color: var(--el-text-color-primary);
  transition: all 0.3s ease;
}

.advanced-settings {
  padding: 16px;
}

.advanced-settings h3 {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 500;
}
</style>

<!-- 全局主题样式 -->
<style>
:root {
  --el-bg-color: #ffffff;
  --el-bg-secondary: #f5f7fa;
  --el-text-color-primary: #303133;
  --el-border-color-light: #dcdfe6;
}

body {
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
  transition:
    background-color 0.3s ease,
    color 0.3s ease;
}
</style>
