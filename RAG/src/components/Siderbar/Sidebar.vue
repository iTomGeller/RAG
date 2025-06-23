<template>
  <div class="sidebar">
    <div class="top">
      <img @click="toggleExtended" class="menu" :src="assets.menu_icon" alt="Menu Icon" />
      <div @click="newChat()" class="new-chat">
        <img :src="assets.plus_icon" alt="Plus Icon" />
        <p v-if="extended">New Chat</p>
      </div>
      <div v-if="extended" class="recent">
        <p class="recent-title">Recent</p>
        <div
          v-for="(item, index) in prevPrompts"
          :key="index"
          @click="loadPrompt(item)"
          class="recent-entry"
        >
          <img :src="assets.message_icon" alt="Message Icon" />
          <p>{{ item.slice(0, 16) }} ...</p>
        </div>
      </div>
    </div>
    <div class="bottom">
      <div class="bottom-item recent-entry">
        <img :src="assets.question_icon" alt="Question Icon" />
        <p v-if="extended">Help</p>
      </div>
      <div class="bottom-item recent-entry">
        <img :src="assets.history_icon" alt="History Icon" />
        <p v-if="extended">Activity</p>
      </div>
      <div class="bottom-item recent-entry">
        <img :src="assets.setting_icon" alt="Setting Icon" />
        <p v-if="extended">Settings</p>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import { assets } from '../../assets/assets'; // Assuming assets path remains the same

const onSent = async (prompt) => {
  console.log('Sending prompt from sidebar:', prompt);
};

const prevPrompts = ref(['What is a singleton?', 'Explain quantum entanglement', 'Write a short story about AI']);
const setRecentPrompt = (prompt) => {
  console.log('Setting recent prompt:', prompt);
};
const newChat = () => {
  console.log('Starting a new chat');
};

const extended = ref(false);

const toggleExtended = () => {
  extended.value = !extended.value;
};

const loadPrompt = async (prompt) => {
  setRecentPrompt(prompt);
  await onSent(prompt);
};
</script>

<style scoped>
@import './Sidebar.css'; /* If you want to keep the external CSS file */

</style>