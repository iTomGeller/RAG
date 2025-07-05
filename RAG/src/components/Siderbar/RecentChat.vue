<template>
  <div
    class="chat-card"
    @click="handleClick"
    :class="{
      'current-chat': isCurrentChat,
    }"
  >
    <div class="chat-info">
      <div class="chat-card-title">{{ formattedDate }}</div>
    </div>
    <div class="delete-button" @click.stop="handleDeleteClick">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="chat-card-icon">
          <el-icon>
            <MoreFilled />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="delete">删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<script setup>
import { ElDropdown, ElDropdownMenu, ElDropdownItem, ElIcon } from 'element-plus'
import { onMounted, computed } from 'vue'
import { MoreFilled } from '@element-plus/icons-vue'
import router from '@/router'
import { ChatService } from '@/service/ChatService'
import { ElMessage } from 'element-plus'

const props = defineProps({
  memoryId: String,
  name: String,
})
const handleCommand = (command) => {
  if (command === 'delete') {
    ChatService.deleteChat(props.memoryId)
      .then(() => {
        ElMessage.success('删除成功')
      })
      .catch((error) => {
        ElMessage.error('删除失败: ' + error.message)
      })
  }
}
const handleClick = async () => {
  ChatService.changeCurrentChat(props.memoryId)
  router.push('/home/chat')
}
const formattedDate = computed(() => {
  return new Date(props.memoryId)
    .toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    })
    .replace(/\//g, '-')
})
const isCurrentChat = computed(() => {
  return ChatService.getCurrentChatId() === props.memoryId
})
onMounted(() => {})
</script>
<style scoped>
.delete-button {
  margin: 4px 4px;
  padding: 6px 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  cursor: pointer;
}
.delete-button:hover {
  background-color: #bababa;
}
.chat-card {
  margin-top: 5px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 20px;
  color: #282828;
  cursor: pointer;
}
.chat-card.current-chat {
  background-color: #d5d8dc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

body.dark .chat-card {
  background-color: #e2e6eb;
}
body.dark .chat-card.current-chat {
  background-color: #6f8096;
}
.chat-card-icon {
  border-radius: 50%;
}
.chat-info {
  padding-left: 5px;
  width: 85%;
  display: flex;
}
.chat-card-title {
  padding: 10px;
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
.current-chat .chat-card-title {
  color: #000;
  background: none;
}
body.dark .current-chat .chat-card-title {
  color: #ffffff;
  background: none;
}
.chat-card:hover {
  background-color: #d5d8dc;
}
</style>
