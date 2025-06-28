// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createI18n } from 'vue-i18n'
import zh from './i18n/zh.json'
import en from './i18n/en.json'


const messages = {
  zh,
  en,
}

// 全局注册语言包
const i18n = createI18n({
  legacy: false, //兼容组合式API
  locale:localStorage.getItem('language') || 'en', //设定本地语系
  fallbackLocale: 'zh', //缺少中文翻译时回退到英文
  messages:{
    en,
    zh
  },//加载语言包
  globalInjection: true//全局注册
});

const app = createApp(App)



//注册到ElementPlus组件库
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(ElementPlus)
app.use(i18n)


app.mount('#app')
