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
            <el-switch v-model="isDarkMode" @change="applyTheme" :active-text="t('settings.dark')"
              inactive-color="#f5f7fa" active-color="#1a1a1a" />
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
                  <el-color-picker v-model="primaryColor" @change="applyTheme()" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="t('settings.bgTransparency')">
                  <el-slider v-model="bgOpacity" :min="0" :max="100" @change="applyTheme()" />
                </el-form-item>
              </el-col>


            </el-row>
          </el-form>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue';
import { ElButton } from 'element-plus';
import { useI18n } from 'vue-i18n'
import TranslateButton from '@/components/Main/TranslateButton.vue';
import ThemeController from '@/components/utils/themeChange';

//全局翻译
const { t } = useI18n()
// 主题状态
const isDarkTheme = ref(false)
const primaryColor = ref('#409eff')
const bgOpacity = ref(100)
const isDarkMode = ref(false);

// 初始化isDarkMode
onMounted(async () => {
  // 1. 尝试从 localStorage 读取用户偏好
  const savedTheme = localStorage.getItem('theme-preference');

  if (savedTheme === 'dark') {
    isDarkMode.value = true;
  } else if (savedTheme === 'light') {
    isDarkMode.value = false;
  } else {
    // 2. 如果没有保存的偏好，检测系统偏好
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDarkMode.value = true;
    }
  }

  const _primaryColor = localStorage.getItem('el-color-primary')
  if (_primaryColor)
    primaryColor.value = _primaryColor

  bgOpacity.value = Number(localStorage.getItem('opacity'))
});

const applyTheme = () => {
  const dark = isDarkMode.value;
  localStorage.setItem('theme-preference', dark ? 'dark' : 'light');
  localStorage.setItem('el-color-primary', primaryColor.value);
  localStorage.setItem('opacity', bgOpacity.value);
  localStorage.setItem('opacity-hex', getOpacityValue());
  ThemeController.changeTheme();
  console.log("bgOpacity", bgOpacity.value)
  console.log('Applied theme:', dark ? 'dark' : 'light');
};

// 获取背景透明度值
const getOpacityValue = () => {
  const opacity = bgOpacity.value / 100
  const hex = Math.round(opacity * 255).toString(16)
  return hex.length === 1 ? '0' + hex : hex
}

// 重置主题
const resetTheme = () => {
  isDarkMode.value = false
  primaryColor.value = '#409eff'
  bgOpacity.value = 100
  applyTheme()
  localStorage.removeItem('theme')
}

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
