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
  const res = await inisiateChat({ memoryId: props.memoryId, message: '你好' })
}
</script>
<style scoped></style>
