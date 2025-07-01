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
            <span>Hi, {{ getUserName() }}</span>
          </p>
          <p>How can I help you today?</p>
        </div>
        <SuggestCards @update:suggestCardsOnSent="handleSuggest" />
      </div>
      <div v-else class="result">
        <div class="result-title">
          <img :src="assets.user_icon" alt="User Icon" />
          <p>{{ recentPrompt }}</p>
        </div>
        <div class="result-data">
          <!-- <img :src="assets.gemini_icon" alt="Gemini Icon" /> -->
          <img :src="assets.otter_icon" alt="Otter Icon" />
          <div v-if="loading" class="loader">
            <hr />
            <hr />
            <hr />
          </div>
          <p v-else v-html="resultData"></p>
        </div>
      </div>

      <div class="main-bottom">
        <div class="search-box">
          <input
            v-model="input"
            type="text"
            placeholder="Search"
            @keyup.enter="sendButtonVisible && sendMessage()"
          />

          <div>
            <!-- <img :src="assets.gallery_icon" alt="Gallery Icon" /> -->
            <img
              v-if="sendButtonVisible"
              @click="sendMessage()"
              :src="assets.send_icon"
              alt="Send Icon"
              class="send-icon"
            />
          </div>
        </div>
        <p class="bottom-info">Otter AI can make mistakes. Check important info.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, inject, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { assets } from '@/assets/assets'
import SuggestCards from './SuggestCards.vue'
import ProfileFloating from '../Profile/ProfileFloating.vue'
import ChatService from '@/service/ChatService'
import { startSSEChat } from '@/service/apiChat';

// const {
//   onSent,
//   recentPrompt,
//   showResult,
//   resultData,
//   input,
//   loading
// } = inject('geminiContext');
const input = ref('')
const resultData = ref(`<span style="color: red">红色文字</span>`)
const loading = ref(false)
const recentPrompt = ref('')
const currentChatId = inject('currentChatId')
const showResult = inject('showResult')
const chatMessages = ref([]);

const router = useRouter()

const sendButtonVisible = computed(() => input.value.trim() !== '')
let closeConnection = null;

const sendMessage = () => {
  if (!input.value.trim()) return;
  
  console.log('Sending message: ' + input.value)

  // 添加用户消息到聊天记录
  chatMessages.value.push({ type: 'USER', text: input.value });

  showResult.value = true //显示结果

  // 发起 SSE 请求（携带 token）
  closeConnection = startSSEChat(
    currentChatId.value,
    input.value,
    (data) => {
      chatMessages.value.push({ type: 'AI', text: data });
      console.log('SSE 数据:', data);
    },
    (error) => {
      console.error('SSE 错误:', error);
      if (error.message.includes('401')) {
        alert('登录已过期，请重新登录');
        router.push('/login');
      }
    }
  );
  input.value = ''
};
const getRecentPrompt = async () => {
  const res = await ChatService.getChatPrompt()
  recentPrompt.value = res
  console.log(res)
  console.log(recentPrompt.value)
}
const handleSuggest = (suggestinput) => {
  addNewChat()
  input.value = suggestinput
  sendMessage()
}
const addNewChat = async () => {
  currentChatId.value = Date.now()
  showResult.value = false
  router.push('/home/chat')
  console.log('Starting a new chat' + currentChatId.value)
}
// const chatOnSent = async () => {
//   console.log('Sending message: ' + input.value)
//   showResult.value = true //显示结果
//   loading.value = true //加载动画
//   const res = await ChatService.initiateChat({ memoryId: currentChatId.value, message: input.value })
//   input.value = ''
//   console.log(res)
//   loading.value = false
// }
const getUserName = () => {
  const user = JSON.parse(localStorage.getItem('userInfo'))
  return user?.username || 'User'
}
onMounted(() => {
  getRecentPrompt()
})
</script>

<style scoped>
@import './Main.css';

.send-icon {
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

@keyframes loader {
  0% {
    background-position: -800px 0px;
  }

  100% {
    background-position: 800px 0px;
  }
}
</style>
