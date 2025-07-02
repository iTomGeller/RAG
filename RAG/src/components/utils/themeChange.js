import { ref } from 'vue'

const ThemeController = {
  //读取localStorage中的主题设置，并进行应用
  changeTheme() {
    const body = document.body
    const root = document.documentElement

    // 设置el-color-primary的参数
    const primaryColor = localStorage.getItem('el-color-primary')
    root.style.setProperty('--el-color-primary', primaryColor)
    const opacityHex= localStorage.getItem('opacity-hex')
    const opacity = localStorage.getItem('opacity')
    root.style.setProperty('--opacity', opacity/100)


    const isDarkMode = ref(false)
    // 1. 尝试从 localStorage 读取用户偏好
    const savedTheme = localStorage.getItem('theme-preference')
    if (savedTheme === 'dark') {
      isDarkMode.value = true
      body.classList.add('dark')
      root.style.setProperty('--el-bg-color', `#1b1b1b${opacityHex}`)
      body.style.setProperty('background-color', 'var(--el-bg-color-page)')
      body.style.setProperty('color', 'white')
      body.style.setProperty('transition', 'background-color 0.3s ease, color 0.3s ease')
      console.log('First using dark')
    } else if (savedTheme === 'light') {
      isDarkMode.value = false
      body.classList.remove('dark')
      root.style.setProperty('--el-bg-color', ``)
      body.style.setProperty('background-color', '')
      body.style.setProperty('color', '')
      body.style.setProperty('transition', '')
      console.log('First using light')
    } else {
      // 2. 如果没有保存的偏好，检测系统偏好
      if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        isDarkMode.value = true
      }
      // 将用户偏好保存到 localStorage
      console.log('Applied theme:', isDarkMode.value ? 'dark' : 'light')
    }
  },
}

export default ThemeController
