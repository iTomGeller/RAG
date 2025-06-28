<template>
  <div class="file-card" @click="handlePreview">
    <div class="file-info">
      <div class="file-name">{{ props.name }}</div>
    </div>
    <img :src="getIcon(props.url)" class="file-icon" />
  </div>
</template>

<script setup>
import { defineProps, ref } from 'vue'

const props = defineProps({
  url: String,
  name: String,
})
function getIcon(url) {
  if (/\.docx?$/.test(url)) return new URL('@/assets/icons/doc.png', import.meta.url).href
  if (/\.xlsx?$/.test(url)) return new URL('@/assets/icons/xls.png', import.meta.url).href
  if (/\.txt$/.test(url)) return new URL('@/assets/icons/txt.png', import.meta.url).href
  if (/\.md$/.test(url)) return new URL('@/assets/icons/md.png', import.meta.url).href
  if (/\.pdf$/.test(url)) return new URL('@/assets/icons/pdf.png', import.meta.url).href
  return new URL('@/assets/icons/file.png', import.meta.url).href
}

function handlePreview() {
  const encodedUrl = encodeURIComponent(props.url)
  const previewUrl = `https://view.officeapps.live.com/op/view.aspx?src=${encodedUrl}`
  window.open(previewUrl, '_blank')
}
</script>

<style scoped>
.viewer-container {
  margin-top: 20px;
}
</style>

<style scoped>
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
