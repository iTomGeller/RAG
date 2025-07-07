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
          <div class="setting-item-subtitle">{{ t('settings.userTip') }}</div>
          <el-divider />
          <div class="user">
            <!-- 用户设置 -->
            <div class="settings-card avatar">
              <div class="settings-row">
                <img :src="user.avatarUrl" alt="User Avatar" class="user-avatar" />
              </div>
              <div class="settings-row">
                <div class="act-btn upload-btn">
                  <AvatarUpload />
                </div>
              </div>
            </div>
            <div class="settings-card info">
              <div class="settings-row">
                <span>{{ t('login.username') }}</span>
                <span>{{ user.username }}</span>
              </div>
              <div class="settings-row">
                <span>{{ t('login.email') }}</span>
                <span>{{ user.email }}</span>
              </div>
              <div class="settings-row">
                <div class="act-btn" @click="logout">{{ t('profile.logout') }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="settings-item">
          <div class="settings-item-title">
            {{ t('settings.language') }}
          </div>
          <div class="setting-item-subtitle">{{ t('settings.languageTip') }}</div>
          <el-divider />
          <div class="settings-card">
            <!-- 语言设置 -->
            <div class="settings-row">
              <span>{{ t('settings.languageTag') }}</span>
              <TranslateButton />
            </div>
          </div>
        </div>
        <div class="settings-item">
          <div class="settings-item-title">
            {{ t('settings.theme') }}
          </div>
          <div class="setting-item-subtitle">{{ t('settings.themeTip') }}</div>
          <el-divider />
          <div class="settings-card">
            <!-- 主题设置 -->
            <div class="settings-row">
              <span>{{ t('settings.theme') }}</span>
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
              <span class="opacity-tag">{{ t('settings.bgTransparency') }}</span>
              <el-slider v-model="bgOpacity" :min="0" :max="100" @change="applyTheme()" />
            </div>
            <div class="settings-row">
              <span>{{ t('settings.mainColor') }}：</span>
              <el-color-picker v-model="primaryColor" @change="applyTheme()" />
            </div>
            <div class="settings-row">
              <span>{{ t('settings.resetTheme') }}：</span>
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
import AvatarUpload from '@/components/Profile/AvatarUpload.vue'
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
//用户登出
const logout = () => {
  localStorage.removeItem('userInfo')
  location.reload() //跳转
}
</script>

<style scoped>
.testbutton {
  width: 100px;
  height: 60px;
}
.settings-item {
  height: auto;
  padding: 20px;
  background-color: #f0f4f9;
  border-radius: 10px;
  position: relative;
}
body.dark .settings-item {
  background-color: rgba(59, 59, 59, var(--opacity));
}
.settings-title {
  color: #585858;
  font-size: 22px;
  padding: 20px;
  padding-right: 50px;
  white-space: nowrap;
}
body.dark .settings-title {
  color: #c7c7c7;
}
.settings-item-title {
  font-size: 20px;
  font-weight: 600;
  color: #585858;
}
body.dark .settings-item-title {
  color: #cfcfcf;
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
  margin-top: 6vh;
  padding-bottom: 20vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 25px;
}
.settings-center::-webkit-scrollbar {
  display: none;
}
.settings-card {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
body.dark .settings-card {
  background-color: #4a4a4a;
}
.settings-card.avatar {
  width: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.settings-card.info {
  flex: 1;
  width: 100%;
}
.settings-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  color: #333333;
  font-weight: 400;
  padding: 10px 20px;
  cursor: pointer;
}
body.dark .settings-row {
  color: #cfcfcf;
}
.settings-row:hover {
  background-color: #f5f5f5;
}
body.dark .settings-row:hover {
  background-color: #5a5a5a;
}
.opacity-tag {
  width: 200px;
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
.act-btn {
  font-size: 14px;
  font-weight: 500;
  color: #717171;
  border-radius: 10px;
  width: 100%;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
}
body.dark .act-btn {
  color: #a9a9a9;
}
.act-btn.upload-btn {
  width: 100px;
}
</style>
