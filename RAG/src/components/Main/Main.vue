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
          <p><span>Hi, man</span></p>
          <p>How can I help you today?</p>
        </div>
        <SuggestCards />
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
          <input v-model="input" type="text" placeholder="Search" @keyup.enter="sendButtonVisible && onSent(input)" />

          <div>
            <!-- <img :src="assets.gallery_icon" alt="Gallery Icon" /> -->
            <img v-if="sendButtonVisible" @click="onSent(input)"
             :src="assets.send_icon" alt="Send Icon"
              class="send-icon" />
          </div>

        </div>
        <p class="bottom-info">Otter AI can make mistakes. Check important info.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, inject } from 'vue'; 
import { assets } from '@/assets/assets'; 
import SuggestCards from './SuggestCards.vue'; 
import ProfileFloating from '../Profile/ProfileFloating.vue';

const {
  onSent,
  recentPrompt,
  showResult,
  resultData,
  input, 
  loading
} = inject('geminiContext');

const sendButtonVisible = computed(() => input.value.trim() !== '');
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