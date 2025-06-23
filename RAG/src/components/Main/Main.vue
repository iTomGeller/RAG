
<template>
  <div class="main">
    <div class="nav">
      <p>Gemini</p>
      <img :src="assets.user_icon" alt="User Icon" />
    </div>
    <div class="main-container">
      <div v-if="!showResult">
        <div class="greet">
          <p><span>Hello, Megha</span></p>
          <p>How can I help you today?</p>
        </div>
        <div class="cards">
          <div class="card" @click="handleCardClick('Suggest beautiful places to see on an upcoming road trip')">
            <p>Suggest beautiful places to see on an upcoming road trip</p>
            <img :src="assets.compass_icon" alt="Compass Icon" />
          </div>
          <div class="card" @click="handleCardClick('Briefly summarize the main points of a text: Urban planning')">
            <p>Briefly summarize the main points of a text: Urban planning</p>
            <img :src="assets.bulb_icon" alt="Bulb Icon" />
          </div>
          <div class="card" @click="handleCardClick('Brainstorm team bonding activities for our work retreat')">
            <p>Brainstorm team bonding activities for our work retreat</p>
            <img :src="assets.message_icon" alt="Message Icon" />
          </div>
          <div class="card" @click="handleCardClick('Improve the readability of the following code')">
            <p>Improve the readability of the following code</p>
            <img :src="assets.code_icon" alt="Code Icon" />
          </div>
        </div>
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
          <input
            v-model="input"
            type="text"
            placeholder="Search"
            @keyup.enter="sendButtonVisible && onSent(input)"
          />
          <div>
            <img :src="assets.gallery_icon" alt="Gallery Icon" />
            <img :src="assets.mic_icon" alt="Mic Icon" />
            <img
              v-if="sendButtonVisible"
              @click="onSent(input)"
              :src="assets.send_icon"
              alt="Send Icon"
              class="send-icon"
            />
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

const setInput = (value) => {
  input.value = value;
};

const handleCardClick = (prompt) => {
  setInput(prompt);
  onSent(prompt);
};

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