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
        <Message
          v-for="(item, index) in messageContent"
          :key="index"
          :type="item.type"
          :text="item.text"
        />
        <!-- <div class="result-title">
          <img :src="assets.user_icon" alt="User Icon" />
          <p>{{ recentPrompt }}</p>
        </div>
        <div class="result-data">
          <img :src="assets.otter_icon" alt="Otter Icon" />
          <div v-if="loading" class="loader">
            <hr />
            <hr />
            <hr />
          </div>
        </div> -->
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
        <p class="bottom-info">Otter AI can make mistakes. Check important info.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { assets } from '@/assets/assets'
import SuggestCards from './SuggestCards.vue'
import ProfileFloating from '../Profile/ProfileFloating.vue'
import { RemoveFilled } from '@element-plus/icons-vue'
import Message from './Message.vue'
import { ChatService, messageContent, sources, showResult, loading } from '@/service/ChatService'

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
  ChatService.setInput(input.value)
  ChatService.sendMessage()
  input.value = '' // 清空输入框
}
const handleSuggest = (suggestinput) => {
  addNewChat()
  input.value = suggestinput
  sendMessage()
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
onMounted(() => {
  console.log('刷新')
})
onUnmounted(() => {})
</script>

<style scoped>
@import './Main.css';

.send-icon {
  cursor: pointer;
}
.stop-icon {
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

@keyframes loader {
  0% {
    background-position: -800px 0px;
  }

  100% {
    background-position: 800px 0px;
  }
}

.result-title {
  border-radius: 10px;
  background-color: white;
  background-color: rgba(255, 255, 255, var(--opacity));
}

.result-data {
  border-radius: 10px;
  background-color: rgb(255, 255, 255);
  background-color: rgba(255, 255, 255, var(--opacity));
}
</style>
