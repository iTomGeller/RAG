<script setup>
import { assets } from '@/assets/assets'
import { onMounted, ref } from 'vue';
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
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
      user.value = JSON.parse(userInfo);
    }
  })
</script>
<template>
  <div class="message-bubble">
      <img v-if="props.type === 'USER'" :src="user.avatarUrl" alt="User Avatar" class="avatar" />
      <img v-else :src="assets.otter_icon" alt="User Avatar" class="avatar" />
    <div class="message-bubble-content" v-html="props.text"></div>
  </div>
</template>
<style scoped>
.message-bubble {
  width: 100%;
  display: flex;
  margin-bottom: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.message-bubble-content {
  border-radius: 12px;
  padding: 8px 12px;
  max-width: 80%;
}

.message-bubble-content img {
  max-width: 100%;
  height: auto;
}
</style>
