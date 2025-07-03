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
  <div :class="['message-bubble', props.type === 'USER' ? 'user-message' : 'ai-message']">
    <img v-if="props.type !== 'USER'" :src="assets.otter_icon" alt="AI Avatar" class="avatar" />
    <div class="message-content">
      <div class="message-bubble-content" v-html="props.text"></div>
    </div>
    <img v-if="props.type == 'USER'" :src="user.avatarUrl" alt="User Avatar" class="avatar" />
  </div>
</template>
<style scoped>
.message-bubble {
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
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 8px;
}

.message-bubble-content {
  border-radius: 12px;
  padding: 8px 12px;
  background-color: #f0f0f0;
  color: #333;
}

/* 用户消息气泡样式 */
.user-message .message-bubble-content {
  background-color: #1890ff;
  color: white;
}

.message-bubble-content img {
  max-width: 100%;
  height: auto;
}
</style>
