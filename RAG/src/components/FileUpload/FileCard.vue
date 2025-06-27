<template>
  <!-- <el-icon v-if="deleteAble" size="20" style="color:red" @click="handleDelete">
    <Delete />
  </el-icon> -->

  <!-- 图片类型 -->
  <template v-if="isImage(props.url)">
    <img :src="props.url" alt="image" class="chat-image" @click="showImagePreview = true" />
    <div v-if="showImagePreview" class="image-preview-overlay" @click="showImagePreview = false">
      <img :src="props.url" class="image-preview" />
    </div>
  </template>

  <!-- 视频类型 -->
  <template v-else-if="isVideo(props.url)">
    <video controls :src="props.url" class="chat-video" />
  </template>


  <!-- 文档类型 -->
  <template v-else-if="isDocument(props.url)">
    <div class="file-card" @click="handlePreview">
      <div class="file-info">
        <div class="file-name">{{ parsed.fileName }}</div>
        <div class="file-size">{{ parsed.fileSize }}</div>
      </div>
      <img :src="getIcon(props.url)" class="file-icon" />
    </div>
  </template>

  <!-- 其他类型 -->
  <template v-else>
    <a :href="props.url" target="_blank">{{ props.url }}</a>
  </template>
</template>

<script setup>
import {  ref, computed, onMounted } from 'vue';
import { Delete } from '@element-plus/icons-vue'

const props = defineProps(['url', 'fileName']);
const showImagePreview = ref(false);

const parsed = computed(() => parseFileName(props.fileName));

function isImage(url) {
  return /\.(png|jpe?g|gif|bmp|webp)$/i.test(url);
}

function isVideo(url) {
  return /\.(mp4|webm|ogg)$/i.test(url);
}

function isDocument(url) {
  return /\.(docx?|pptx?|xlsx?|pdf)$/i.test(url);
}

function getIcon(url) {
  if (/\.docx?$/.test(url)) return new URL('@/assets/icons/doc.png', import.meta.url).href;
  if (/\.pptx?$/.test(url)) return new URL('@/assets/icons/ppt.png', import.meta.url).href;
  if (/\.xlsx?$/.test(url)) return new URL('@/assets/icons/xls.png', import.meta.url).href;
  if (/\.pdf$/.test(url)) return new URL('@/assets/icons/pdf.png', import.meta.url).href;
  return new URL('@/assets/icons/file.png', import.meta.url).href;
}

function parseFileName(fileName) {
  return fileName;
  // format like "filename-(size)"
  const match = fileName?.match(/^(.*)-\(([^)]+)\)$/);
  if (!match) return { fileName: fileName, fileSize: null };
  return { fileName: match[1], fileSize: match[2] };
}

// Office Online 预览文档
function handlePreview() {
  const encodedUrl = encodeURIComponent(props.url);
  const previewUrl = `https://view.officeapps.live.com/op/view.aspx?src=${encodedUrl}`;
  window.open(previewUrl, '_blank');
}

const handleDelete = async () => {
  await signalRService.deleteMessage(props.messageId)
};

onMounted(() => { 
});
</script>

<style scoped>

.chat-image {
  max-width: 200px;
  border-radius: 8px;
  cursor: pointer;
}

/* 弹出图片预览 */
.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.image-preview {
  max-width: 90%;
  max-height: 90%;
  border-radius: 8px;
}

/* 文件卡片样式 */
.file-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f3f3f3;
  border-radius: 8px;
  padding: 12px;
  width: 250px;
  color: #333;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.file-card:hover {
  background-color: #e0e0e0;
}

.file-info {
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
}

.file-name {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-size {
  font-size: 12px;
  color: #999;
  margin-top: 6px;
}

.file-icon {
  width: 40px;
  height: 40px;
  margin-left: 12px;
  flex-shrink: 0;
}

.chat-video {
  max-width: 300px;
  border-radius: 8px;
  cursor: pointer;
}

</style>
