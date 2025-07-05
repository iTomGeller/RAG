<template>
  <div class="settings-container">
    <div class="settings-title">
      <p>{{ t('settings.title') }}</p>
    </div>
    <div class="settings-center">
      <div class="settings-list">
        <div class="settings-item">
          <div class="settings-item-title">
            {{ t('settings.user') }}
          </div>
          <div class="setting-item-subtitle">在这里设置你的用户信息</div>
          <el-divider />
          <div class="user">
            <!-- 用户设置 -->
            <div class="settings-card avatar">
              <img :src="user.avatarUrl" alt="User Avatar" class="user-avatar" />
            </div>
            <div class="settings-card info">
              <div class="settings-row">
                <span>用户名：</span>
                <span>{{ user.username }}</span>
              </div>
              <div class="settings-row">
                <span>邮箱：</span>
                <span>{{ user.email }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="settings-item">
          <div class="settings-item-title">
            {{ t('settings.language') }}
          </div>
          <div class="setting-item-subtitle">在这里设置你的语言</div>
          <el-divider />
          <div class="settings-card">
            <!-- 语言设置 -->
            <div class="settings-row">
              <span>全局语言：</span>
              <TranslateButton />
            </div>
          </div>
        </div>
        <div class="settings-item">
          <div class="settings-item-title">
            {{ t('settings.theme') }}
          </div>
          <div class="setting-item-subtitle">在这里设置你的主题</div>
          <el-divider />
          <div class="settings-card">
            <!-- 主题设置 -->
            <div class="settings-row">
              <span>主题切换：</span>
              <el-switch
                v-model="isDarkMode"
                @change="applyTheme"
                :active-text="t('settings.dark')"
                :inactive-text="t('settings.light')"
                inactive-color="#f5f7fa"
                active-color="#1a1a1a"
              />
            </div>

            <div class="settings-row">
              <span>背景透明度：</span>
              <el-slider v-model="bgOpacity" :min="0" :max="100" @change="applyTheme()" />
            </div>
            <div class="settings-row">
              <span>主要颜色：</span>
              <el-color-picker v-model="primaryColor" @change="applyTheme()" />
            </div>
            <div class="settings-row">
              <span>恢复默认设置：</span>
              <el-button size="small" icon="refresh" @click="resetTheme">{{
                t('settings.reset')
              }}</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="user-container">
      <ProfileFloating />
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'
import { ElButton } from 'element-plus'
import { useI18n } from 'vue-i18n'
import ThemeController from '@/components/utils/themeChange'
import { ElDivider } from 'element-plus'
import ProfileFloating from '@/components/Profile/ProfileFloating.vue'
import TranslateButton from '@/components/Main/TranslateButton.vue'

//用户信息
const user = ref({
  username: '',
  email: '',
  avatarUrl: '',
})
//全局翻译
const { t } = useI18n()
// 主题状态
const primaryColor = ref('#409eff')
const bgOpacity = ref(100)
const isDarkMode = ref(false)

// 初始化isDarkMode
onMounted(async () => {
  //获取用户信息
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    user.value = JSON.parse(userInfo)
  }
  // 1. 尝试从 localStorage 读取用户偏好
  const savedTheme = localStorage.getItem('theme-preference')

  if (savedTheme === 'dark') {
    isDarkMode.value = true
  } else if (savedTheme === 'light') {
    isDarkMode.value = false
  } else {
    // 2. 如果没有保存的偏好，检测系统偏好
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDarkMode.value = true
    }
  }

  const _primaryColor = localStorage.getItem('el-color-primary')
  if (_primaryColor) primaryColor.value = _primaryColor

  bgOpacity.value = Number(localStorage.getItem('opacity'))
})

const applyTheme = () => {
  const dark = isDarkMode.value
  localStorage.setItem('theme-preference', dark ? 'dark' : 'light')
  localStorage.setItem('el-color-primary', primaryColor.value)
  localStorage.setItem('opacity', bgOpacity.value)
  localStorage.setItem('opacity-hex', getOpacityValue())
  ThemeController.changeTheme()
  console.log('bgOpacity', bgOpacity.value)
  console.log('Applied theme:', dark ? 'dark' : 'light')
}

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
.settings-item {
  height: auto;
  padding: 20px;
  background-color: #f0f4f9;
  border-radius: 10px;
  position: relative;
}
.settings-title {
  color: #585858;
  font-size: 22px;
  padding: 20px;
  padding-right: 50px;
  white-space: nowrap;
}
.settings-item-title {
  font-size: 20px;
  font-weight: 600;
  color: #585858;
}
.setting-item-subtitle {
  font-size: 14px;
  color: #888888;
}
.settings-center {
  width: 900px;
  padding: 0px 5%;
  height: 100vh;
  overflow-y: auto;
  justify-content: center;
  display: flex;
}
.settings-list {
  margin-right: 4vw;
  margin-top: 20vh;
  padding-bottom: 20vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 25px;
}
.settings-card {
  /* padding: 20px; */
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.settings-card.avatar {
  padding: 20px 0px;
  width: 100px;
  display: flex;
  justify-content: center;
}
.settings-card.info {
  flex: 1;
  width: 100%;
}
.settings-row {
  padding: 10px 20px;
  cursor: pointer;
}
.settings-row:hover {
  background-color: #f5f5f5;
}
.user {
  display: flex;
  flex-direction: row;
  gap: 20px;
}
.settings-container {
  height: 100vh;
  background-color: var(--el-bg-color);
  transition: background-color 0.3s ease;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}
.settings-container .user-avatar {
  width: 60px;
  height: 60px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  border-radius: 50%;
}

.settings-container .user-container {
  justify-self: right;
  margin-right: 20px;
}

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

.theme-card {
  margin-top: 16px;
  border-radius: 12px;
  overflow: hidden;
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
