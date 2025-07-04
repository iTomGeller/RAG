<script setup>
import { assets } from "@/assets/assets"
import { onMounted, ref } from "vue";
const props = defineProps({
  type: {
    type: Object,
    required: true,
  },
  text: {
    type: String,
    required: true,
  },
})
const user = ref({
  avatarUrl: assets.default_avatar,
});
onMounted(() => {
    const userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      user.value = JSON.parse(userInfo);
    }
  })
</script>
<template>
  <div class="message-bubble" :class=" {
    'user-message': props.type === 'USER',
    'ai-message': props.type === 'AI'
  }">
    <img v-if="props.type !== 'USER'" :src="assets.otter_icon" alt="AI Avatar" class="avatar" />
    <div class="message-content">
      <div class="message-bubble-content" v-html="props.text"></div>
    </div>
  </div>
</template>
<style scoped>
.message-bubble {
  font-size: large;
  font-weight: 300;
  line-height: 1.5;
  margin-top: 30px;
  width: 100%;
  display: flex;
  margin-bottom: 12px;
  align-items: start;
}

.message-bubble .message-content {
  max-width: 80%;
}

.user-message {
  justify-content: flex-end;
}

.ai-message {
  justify-content: flex-start;
}

.avatar {
  width: 35px;
  border-radius: 50%;
  border: 10px solid rgb(227, 247, 255);
  margin: 0 8px;
}

.message-bubble-content {
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  padding: 4px 18px;
  background-color: #f9f9f9;
  color: #333;
}

/* 用户消息气泡样式 */
.user-message .message-bubble-content {
  border-top-right-radius: 0;
  background-color: #dfeffd;
}

.message-bubble-content img {
  max-width: 100%;
  height: auto;
}
</style>
