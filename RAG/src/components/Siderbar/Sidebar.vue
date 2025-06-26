<template>
  <div class="sidebar" ref="sidebarRef">
    <div class="top">
      <!-- 菜单图标 -->
      <el-icon class="menu" :size="iconSize" @click="toggleExtended">
        <Menu />
      </el-icon>

      <!-- Recent 内容包裹在 transition 中 -->
      <!-- transition 是动画渐变组件，控制淡出效果 -->
      <transition name="fade-slide" mode="out-in">
        <div v-show="extended" class="recent">
          <p class="recent-title">最近</p>
          <div
            v-for="(item, index) in prevPrompts"
            :key="index"
            @click="loadPrompt(item)"
            class="recent-entry"
          >
            <el-icon :size="iconSize">
              <ChatSquare />
            </el-icon>
            <p>{{ item.slice(0, 16) }}</p>
          </div>
        </div>
      </transition>
    </div>

    <div class="bottom">
      <!-- 创建新对话 -->
      <div class="bottom-item recent-entry" @click="handleChat">
        <el-icon :size="iconSize">
          <ChatDotSquare />
        </el-icon>
        <transition name="fade-slide">
          <p class="recent-tag" v-show="extended">新建对话</p>
        </transition>
      </div>

      <!-- 回顾历史 -->
      <div class="bottom-item recent-entry">
        <el-icon :size="iconSize">
          <Clock />
        </el-icon>
        <transition name="fade-slide">
          <p class="recent-tag" v-show="extended">历史对话</p>
        </transition>
      </div>

      <!-- 设置 -->
      <div class="bottom-item recent-entry" @click="handleSettings">
        <el-icon :size="iconSize">
          <Setting />
        </el-icon>
        <transition name="fade-slide">
          <p class="recent-tag" v-show="extended">设置</p>
        </transition>
      </div>

      <!-- 知识库 -->
      <div class="bottom-item recent-entry" @click="handleStore">
        <el-icon :size="iconSize">
          <Star />
        </el-icon>
        <transition name="fade-slide">
          <p class="recent-tag" v-show="extended">知识库</p>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, inject } from 'vue' // Import inject
import { assets } from '../../assets/assets'

import { Menu, ChatSquare, ChatDotSquare, Clock, Setting, Star } from '@element-plus/icons-vue'

import { useRouter } from 'vue-router'

const router = useRouter()

const { prevPrompts, newChat } = inject('geminiContext') // Destructure the needed properties and methods

import { gsap } from 'gsap' // 引入GSAP

const iconSize = 25

const extended = ref(false)
const sidebarRef = ref(null)
const emit = defineEmits(['update:extended']) // 定义自定义事件

const toggleExtended = () => {
  extended.value = !extended.value
  emit('update:extended', extended.value) // 触发事件并传递当前状态
  if (sidebarRef.value) {
    gsap.to(sidebarRef.value, {
      duration: 0.1,
      width: extended.value ? '200px' : '75px',
      ease: 'power2.out',
      transformOrigin: 'right center',
    })
  }
}

const loadPrompt = async (prompt) => {
  console.log('点击历史', prompt)
  // 加载往期历史，未实现
}

const handleChat = () => {
  router.push('/home/chat')
}

const handleStore = () => {
  router.push('/home/store')
}
const handleSettings = () => {
  router.push('/home/settings')
}
</script>

<style scoped>
@import './Sidebar.css';
.fade-slide-enter-active {
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease 0.2s; /* 延迟 0.2s 后开始动画 */
}
.fade-slide-enter-to {
  opacity: 1;
  transform: translateX(0);
}
.fade-slide-leave-active {
  opacity: 1;
  transform: translateX(0);
  transition: opacity 0.2s ease;
}
.fade-slide-leave-to {
  opacity: 0;
}
</style>
