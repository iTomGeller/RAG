<template>
  <div class="main">
    <div class="nav">
      <p>Otter AI</p>
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
      <div v-else class="result" ref="resultRef">
        <Message
          v-for="(item, index) in messageContent"
          :key="index"
          :type="item.type"
          :text="item.text"
        />
        <div class="result-spacer"></div>
      </div>

      <FeedbackDrawer v-model:visible="isDrawerVisible" />

      <div class="main-bottom">
        <transition name="scale">
          <div v-if="hasSource && !sourceHiden" class="source" ref="sourceRef">
            <div class="source-title-box" @click="handleSourceCardClick">
              <el-icon color="#ffffff" class="source-icon"><ArrowDownBold /></el-icon>
              <span class="source-title">{{ t('chat.sourceTip') }}</span>
              <div class="source-hide" @click.stop="handleHideButtonClick">
                <el-icon size="small" color="grey"><CloseBold /></el-icon>
              </div>
            </div>
            <span
              class="source-cards"
              :class="{ 'two-columns': shouldUseTwoColumns }"
              v-show="sourceExtended"
            >
              <transition-group name="scale" mode="out-in">
                <FileCard
                  v-for="(file, index) in sources"
                  :key="index"
                  :url="file.url"
                  :name="file.title"
                  class="source-card"
                  v-show="sourceExtended"
              /></transition-group>
            </span>
          </div>
        </transition>
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
    <div class="user-container">
      <ProfileFloating />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch, nextTick } from 'vue'
import { assets } from '@/assets/assets'
import { CloseBold } from '@element-plus/icons-vue'
import { ArrowDownBold } from '@element-plus/icons-vue'
import SuggestCards from './SuggestCards.vue'
import ProfileFloating from '../Profile/ProfileFloating.vue'
import { RemoveFilled } from '@element-plus/icons-vue'
import Message from './Message.vue'
import FeedbackDrawer from '../Siderbar/FeedbackDrawer.vue'
import { ChatService, messageContent, showResult, loading, sources } from '@/service/ChatService'
import FileCard from '../FileUpload/FileCard.vue'
import { gsap } from 'gsap' // 引入GSAP
import { useI18n } from 'vue-i18n' //全局语言切换
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

const sourceHiden = ref(false) //手动控制显资源框
const sendMessage = () => {
  sourceHiden.value = false
  if (sourceExtended.value === true) {
    toggleSourceCard()
  }
  ChatService.setInput(input.value)
  ChatService.sendMessage()
  input.value = '' // 清空输入框
}
const handleSuggest = (suggestinput) => {
  addNewChat()
  input.value = suggestinput
  sendMessage()
}
const sourceRef = ref(null) //资源框
const resultRef = ref(null) //对话列表
let isUserScrolling = false
resultRef.value?.addEventListener('scroll', () => {
  const { scrollTop, scrollHeight, clientHeight } = resultRef.value
  isUserScrolling = scrollTop + clientHeight < scrollHeight - 50
})

// 智能滚动逻辑
watch(
  messageContent,
  async () => {
    await nextTick()
    if (!isUserScrolling && resultRef.value) {
      const smoothScroll = () => {
        resultRef.value.scrollTo({
          top: resultRef.value.scrollHeight,
          behavior: 'smooth',
        })
      }

      // 性能优化：使用RAF减少重绘
      requestAnimationFrame(() => {
        requestAnimationFrame(smoothScroll)
      })
    }
  },
  { deep: true },
)
const scrollToBottom = () => {
  if (resultRef.value && sourceExtended.value) {
    resultRef.value.scrollTo({
      top: resultRef.value.scrollHeight,
      behavior: 'smooth',
    })
  } else if (resultRef.value) {
    resultRef.value.scrollBy({
      top: -300,
      behavior: 'smooth',
    })
  }
}
const handleSourceCardClick = () => {
  toggleSourceCard()
}
const handleHideButtonClick = () => {
  sourceHiden.value = true
}
const toggleSourceCard = async () => {
  sourceExtended.value = !sourceExtended.value
  if (sourceRef.value) {
    const sourceAmount = sources.value.length
    gsap.to(sourceRef.value, {
      duration: 0.1,
      width: sourceExtended.value ? `${sourceAmount < 4 ? 300 : 600}px` : '300px',
      height: sourceExtended.value ? `${getSourceRow(sourceAmount)}px` : '45px',
      marginBottom: sourceExtended.value ? '60px' : '10px',
      ease: 'power2.out',
      transformOrigin: 'top center',
    })
  }
}
const shouldUseTwoColumns = computed(() => sources.value.length > 3)
const getSourceRow = (amount) => {
  if (amount === 1) {
    return 100
  } else if (amount === 2 || amount === 4) {
    return 200
  } else {
    return 300
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
  margin-bottom: 10px;
  border-radius: 15px;
  background-color: #67c23a;
}
body.dark .source {
  background-color: #409eff;
}
.source-title-box {
  align-items: center;
  margin-bottom: 10px;
  padding: 10px 0px;
  border-radius: 10px;
  width: auto;
  display: flex;
  justify-content: space-between;
}
.source:hover {
  background-color: #69ae47;
}
body.dark .source:hover {
  background-color: #2a598a;
}
.source-title {
  font-size: 16px;
  font-weight: bold;
  color: #ffffff;
}
.source-icon {
  margin-left: 15px;
  font-size: 18px;
}
.source-cards {
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin: 0px 15px;
  display: grid;
  background-color: #ffffff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
  grid-template-columns: repeat(1, 1fr);
  grid-template-rows: 100px;
}
body.dark .source-cards {
  background-color: #49494a;
}
.source-cards .two-columns {
  grid-template-columns: repeat(2, 1fr);
}
.source-card {
  justify-self: center;
  margin: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.source-hide {
  margin-right: 10px;
  margin-left: 60px;
  width: 25px;
  height: 25px;
  border-radius: 100%;
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
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
.result-spacer{
  height: 100px;
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
