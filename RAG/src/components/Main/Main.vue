<template>
  <div class="main">

    <div class="nav">
      <p>Gemini</p>
      <img :src="assets.user_icon" alt="User Icon" />
    </div>

    <div class="main-container">

      <div v-if="!showResult">
        <div class="greet">
          <p><span>Hello, human</span></p>
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
          <img :src="assets.gemini_icon" alt="Gemini Icon" />
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
            <img :src="assets.gallery_icon" alt="Gallery Icon" />
            <img v-if="sendButtonVisible" @click="onSent(input)" :src="assets.send_icon" alt="Send Icon"
              class="send-icon" />
          </div>
        </div>
        <p class="bottom-info">Gemini can make mistakes. Check important info.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { assets } from '@/assets/assets'; // Assuming assets path remains the same
import SuggestCards from './SuggestCards.vue';

const onSent = (value) => {
  loading.value = true;
  showResult.value = true;
  recentPrompt.value = value;

  setTimeout(() => {
    resultData.value = `This is a simulated response for: <b>${value}</b>. <br><br> Vue is awesome!`;
    loading.value = false;
  }, 1500);
};

const recentPrompt = ref('');
const showResult = ref(false);
const resultData = ref('');
const input = ref('');
const loading = ref(false);


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