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
import { inject, onMounted } from 'vue'
import { MoreFilled } from '@element-plus/icons-vue'
import ChatService from '@/service/ChatService'
import { useRouter } from 'vue-router'
const router = useRouter()

const currentChatId = inject('currentChatId')
const showResult = inject('showResult')

const props = defineProps({
  memoryId: String,
  name: String,
})
const handleClick = async () => {
  currentChatId.value = props.memoryId
  showResult.value = true
  router.push('/home/chat') //跳转到chat页面
  console.log('turn to chat' + props.name + ', memoryID=' + currentChatId.value)
}
onMounted(() => {
  console.log(props.memoryId === currentChatId.value)
})
</script>
<style scoped>
.chat-card {
  margin-top: 5px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-radius: 20px;
  color: #282828;
  /* background-color: #e2e6eb; */
  cursor: pointer;
}
.chat-info {
  padding-left: 5px;
  width: 70%;
  display: flex;
}
.chat-card-title {
  font-size: 15px;
  font-weight: 350;
  width: 100%;
  background: linear-gradient(
    to right,
    black 0%,
    black 50%,
    rgba(0, 0, 0, 0.5) 90%,
    transparent 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  white-space: nowrap;
}
.chat-card:hover {
  background-color: #d5d8dc;
}
</style>
