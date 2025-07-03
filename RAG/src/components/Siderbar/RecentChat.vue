<template>
  <div class="chat-card">
    <div class="chat-info"  @click="handleClick">
      <div class="chat-card-title">{{ props.name }}</div>
    </div>
    <el-dropdown  trigger="click" @command="handleCommand">
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
</template>
<script setup>
import { ElDropdown, ElDropdownMenu, ElDropdownItem, ElIcon } from 'element-plus'
import { inject, onMounted } from 'vue'
import { MoreFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
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
}
onMounted(() => {})
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
  cursor: pointer;
}

body.dark .chat-card{
  background-color: #e2e6eb;
}
.chat-card-icon {
  border-radius: 50%;
}
.chat-card-icon:hover {
  background-color: #d5d8dc;
}
.chat-info {
  padding-left: 5px;
  width: 85%;
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
