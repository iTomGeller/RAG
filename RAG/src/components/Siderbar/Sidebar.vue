<template>
  <div class="sidebar" ref="sidebarRef">

    <div class="top">
      <!-- <img @click="toggleExtended" class="menu" :src="assets.menu_icon" alt="Menu Icon" /> -->
      <el-icon class="menu" :size="iconSize" @click="toggleExtended">
        <Menu />
      </el-icon>

      <!-- <div @click="newChat()" class="new-chat">
        <img :src="assets.plus_icon" alt="Plus Icon" />
        <p v-if="extended">New Chat</p>
      </div> -->

      <div v-if="extended" class="recent">
        <p class="recent-title">Recent</p>

        <div v-for="(item, index) in prevPrompts" :key="index" @click="loadPrompt(item)" class="recent-entry">
          <el-icon :size="iconSize">
            <ChatSquare />
          </el-icon>
          <p>{{ item.slice(0, 16) }} </p>
        </div>

      </div>

    </div>

    <div class="bottom">

      <!-- 创建新对话 -->
      <div class="bottom-item recent-entry" @click="handleChat">
        <el-icon :size="iconSize">
          <ChatDotSquare />
        </el-icon>
        <p v-if="extended">New Chat</p>
      </div>

      <!-- 回顾历史 -->
      <div class="bottom-item recent-entry">
        <el-icon :size="iconSize">
          <Clock />
        </el-icon>
        <p v-if="extended">Activity</p>
      </div>

      <!-- 设置 -->
      <div class="bottom-item recent-entry" @click="handleSettings">
        <el-icon :size="iconSize">
          <Setting />
        </el-icon>
        <p v-if="extended">Settings</p>
      </div>


      <!-- 知识库 -->
      <div class="bottom-item recent-entry" @click="handleStore">
        <el-icon :size="iconSize">
          <Star />
        </el-icon>
        <p v-if="extended">Store</p>
      </div>

    </div>
  </div>
</template>


<script setup>
import { ref, inject } from 'vue'; // Import inject
import { assets } from '../../assets/assets';

import {
  Menu,
  ChatSquare,
  ChatDotSquare,
  Clock,
  Setting,
  Star
} from '@element-plus/icons-vue';

import { useRouter } from 'vue-router'

const router = useRouter()


const {
  prevPrompts,
  newChat
} = inject('geminiContext'); // Destructure the needed properties and methods

import { gsap } from 'gsap'; // 引入GSAP

const iconSize = 25;

const extended = ref(false);
const sidebarRef = ref(null);

const toggleExtended = () => {
  extended.value = !extended.value;

  if (sidebarRef.value) {
    gsap.to(sidebarRef.value, {
      duration: 0.1,
      width: extended.value ? '200px' : '70px',
      ease: 'power2.out',
      transformOrigin: 'right center'
    });
  }
};

const loadPrompt = async (prompt) => {  
  console.log("点击历史", prompt)
  // 加载往期历史，未实现
};

const handleChat = () => {
  router.push('/home/chat')
};

const handleStore = () => {
  router.push('/home/store')
};
const handleSettings = () => {
  router.push('/home/settings')
};
</script>


<style scoped>
@import './Sidebar.css';
</style>
