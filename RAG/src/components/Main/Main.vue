<template>
  <div class="main">
    <div class="nav">
      <p>Otter AI</p>
      <!-- User Profile -->
      <ProfileFloating />
    </div>

    <div class="main-container">
      <div v-if="!showResult">
        <div class="greet">
          <p>
            <span>{{ t('chat.aiTitle') }} {{ getUserName() }}</span>
          </p>
          <p>{{ t('chat.aiHello') }}</p>
        </div>
        <SuggestCards @update:suggestCardsOnSent="handleSuggest" />
      </div>
      <div v-else class="result">
        <Message
          v-for="(item, index) in messageContent"
          :key="index"
          :type="item.type"
          :text="item.text"
        />
        <transition name="scale">
          <div v-show="hasSource" class="source" ref="sourceRef">
            <div class="source-title-box" @click="handleSourceCardClick">
              <span class="source-title">{{ t('chat.sourceTip') }}</span>
              <el-icon color="#ffffff" class="source-icon"><ArrowDownBold /></el-icon>
            </div>
            <span class="source-cards" v-show="sourceExtended">
              <transition-group name="scale" mode="out-in">
                <FileCard
                  v-for="(file, index) in sources"
                  :key="index"
                  :url="file.url"
                  :name="file.title"
                  class="source-card"
                  v-show="sourceExtended"
              /></transition-group>
            </span></div
        ></transition>
      </div>

            <FeedbackDrawer v-model:visible="isDrawerVisible" />

      <div class="main-bottom">
        <div class="search-box">
          <input
            v-model="input"
            type="text"
            :placeholder="t('chat.searchAlt')"
            @keyup.enter="sendButtonVisible && sendMessage()"
          />

          <div>
            <img
              v-if="sendButtonVisible"
              @click="sendMessage()"
              :src="assets.send_icon"
              alt="Send Icon"
              class="send-icon"
            />
            <el-icon v-if="showResult && loading" @click="stopChat()" class="stop-icon">
              <RemoveFilled />
            </el-icon>
          </div>
        </div>
        <p class="bottom-info">{{ t('chat.aiTip') }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { assets } from '@/assets/assets'
import { ArrowDownBold } from '@element-plus/icons-vue'
import SuggestCards from './SuggestCards.vue'
import ProfileFloating from '../Profile/ProfileFloating.vue'
import { RemoveFilled } from '@element-plus/icons-vue'
import Message from './Message.vue'
import FeedbackDrawer from '../Siderbar/FeedbackDrawer.vue'
import {
  ChatService,
  messageContent,
  showResult,
  loading,
} from '@/service/ChatService'
import FileCard from '../FileUpload/FileCard.vue'
import { gsap } from 'gsap' // 引入GSAP
import { useI18n } from 'vue-i18n'//全局语言切换
const { t } = useI18n()

// const {
//   onSent,
//   recentPrompt,
//   showResult,
//   resultData,
//   input,
//   loading
// } = inject('geminiContext');


const input = ref('')
const sendButtonVisible = computed(() => input.value.trim() !== '')
const sendMessage = () => {
  sourceExtended.value = false //发送消息时，让source列表回归默认状态
  ChatService.setInput(input.value)
  ChatService.sendMessage()
  input.value = '' // 清空输入框
}
const handleSuggest = (suggestinput) => {
  addNewChat()
  input.value = suggestinput
  sendMessage()
}
const sourceRef = ref(null)
const handleSourceCardClick = async () => {
  sourceExtended.value = !sourceExtended.value
  if (sourceRef.value) {
    gsap.to(sourceRef.value, {
      duration: 0.1,
      width: sourceExtended.value ? '600px' : '300px',
      height: sourceExtended.value ? '300px' : '45px',
      ease: 'power2.out',
      transformOrigin: 'top center',
    })
  }
}
const addNewChat = async () => {
  ChatService.addNewChat()
}
const stopChat = () => {
  ChatService.stopChat()
}

const getUserName = () => {
  const user = JSON.parse(localStorage.getItem('userInfo'))
  return user?.username || 'User'
}
const hasSource = computed(() => sources.value.length > 0)
const sourceExtended = ref(false)
onMounted(() => {
  console.log('刷新')
})
onUnmounted(() => {})
</script>

<style scoped>
@import './Main.css';
.source {
  cursor: pointer;
  transition: all 0.2s ease;
  justify-self: center;
  width: 300px;
  height: 45px;
  margin-top: 20px;
  border-radius: 15px;
  background-color: #67C23A;
  margin-bottom: 100px;
}
.source-title-box {
  align-items: center;
  margin-bottom: 10px;
  padding: 10px 32px;
  border-radius: 10px;
  width: auto;
  display: flex;
  justify-content: space-between;
}
.source:hover {
  background-color: #69ae47;
}
.source-title {
  font-size: 16px;
  font-weight: bold;
  color: #ffffff;
}
.source-icon{
  font-size: 18px;
}
.source-cards {
  padding: 4px;
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin: 0px 15px;
  display: grid;
  background-color: #ffffff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}
.source-card {
  justify-self: center;
  margin: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.send-icon {
  cursor: pointer;
}
.stop-icon {
  font-size: 24px;
  cursor: pointer;
}

.loader {
  width: 100%;
  background-color: #f6f7f8;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.loader hr {
  border: none;
  height: 20px;
  background: linear-gradient(to right, #9ed7ff, #ffffff, #9ed7ff);
  background-size: 800px 50px;
  animation: loader 3s infinite linear;
}
.chat-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 10px;
  height: 80vh;
  display: flex;
  flex-direction: column;
}
/* 缩放渐变 */
.scale-enter-active,
.scale-leave-active {
  transition: all 0.5s ease;
}
.scale-enter-from,
.scale-leave-to {
  transform: scale(0);
  opacity: 0;
}
/*淡出渐变*/
.fade-slide-enter-active {
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease 0.2s;
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

@keyframes loader {
  0% {
    background-position: -800px 0px;
  }

  100% {
    background-position: 800px 0px;
  }
}
</style>
