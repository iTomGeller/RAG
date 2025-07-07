<template>
  <div class="file-card" @click="handlePreview">
    <div class="file-info">
      <div class="file-name">{{ props.name }}</div>
    </div>
    <img :src="getIcon(props.url)" class="file-icon" />
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import {  ref } from 'vue'

const props = defineProps({
  url: String,
  name: String,
})
function getIcon(url) {
  if (/\.docx?$/.test(url)) return new URL('@/assets/file-icons/doc.png', import.meta.url).href
  if (/\.xlsx?$/.test(url)) return new URL('@/assets/file-icons/xls.png', import.meta.url).href
  if (/\.txt$/.test(url)) return new URL('@/assets/file-icons/txt.png', import.meta.url).href
  if (/\.md$/.test(url)) return new URL('@/assets/file-icons/md.png', import.meta.url).href
  if (/\.pdf$/.test(url)) return new URL('@/assets/file-icons/pdf.png', import.meta.url).href
  return new URL('@/assets/file-icons/file.png', import.meta.url).href
}

function handlePreview() {
  console.log("Original URL:", props.url);
  const fileExtension = props.url.split('.').pop().toLowerCase();
  let previewUrl = '';

  switch (fileExtension) {
    case 'docx':
      previewUrl = `https://view.officeapps.live.com/op/view.aspx?src=${encodeURIComponent(props.url)}`;
      break;
    case 'pdf':
      // 方案 A: 使用 Google Docs Viewer
        // previewUrl = `https://docs.google.com/viewerng/viewer?url=${encodeURIComponent(props.url)}`;
      // 方案 B: 直接在浏览器中打开（现代浏览器通常内置PDF阅读器）
      previewUrl = props.url;
      break;
    case 'txt':
      previewUrl = props.url;
      break;
    default:
      console.warn("Unsupported file type for preview:", fileExtension);
      ElNotification({
        message: "此文件类型不支持在线预览。将尝试直接打开。",
      });
      previewUrl = props.url; // 直接打开原始URL
      break;
  }

  if (previewUrl) {
    window.open(previewUrl, '_blank');
  }
}
</script>


<style scoped>
.viewer-container {
  margin-top: 20px;
}

.chat-image {
  max-width: 200px;
  border-radius: 8px;
  cursor: pointer;
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
