<template>
  <div class="chat-card" @click="handleClick">
    <div class="chat-info">
      <div class="chat-card-title">{{ props.name }}</div>
    </div>
    <el-icon>
      <MoreFilled />
    </el-icon>
  </div>
</template>
<script setup>
import { MoreFilled } from '@element-plus/icons-vue'
import ChatService from '@/service/ChatService'

const props = defineProps({
  memoryId: String,
  name: String,
})
const handleClick = async () => {
  // const res = await inisiateChat({ memoryId: props.memoryId, message: '你好' })
  console.log('turn to chat' + props.name)
}
</script>
<style scoped>
.chat-card {
  margin-top: 5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-radius: 20px;
  color: #282828;
  background-color: #e2e6eb;
  cursor: pointer;
}
.chat-card:hover {
  background-color: #d5d8dc;
}
.chat-info {
  padding-left: 5px;
  display: flex;
}
.chat-card-title {
  font-size: 15px;
  font-weight: 350;
}
</style>
