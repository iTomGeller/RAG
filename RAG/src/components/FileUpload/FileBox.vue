<template>
  <ExpandWindow :title="props.name" auto-size="true" @open="windowOpen" @close="windowClose">
    <template #button>
      <div class="wolfram-card" @click="handleClick">
        <div class="wolfram-card__icon-wrapper">
          <img :src="iconUrl" :alt="title + ' Icon'" class="wolfram-card__icon" />
        </div>
        <div class="wolfram-card__content">
          <h3 class="wolfram-card__title">{{ props.name }}</h3>
        </div>
      </div>
    </template>
    <template #content>
      <div v-if="total > 0" class="file-cards-grid" :class="{ 'single-item-center': total === 1 }">
        <FileCard
          class="file-card"
          v-for="file in files"
          :key="file.id"
          :url="file.url"
          :name="file.name"
        />
      </div>
      <div v-else class="empty-file-box"></div>

      <!-- 分页控件 -->
      <div v-if="total > 0" class="file-cards-pagination-controls-container">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          :layout="'prev, pager, next'"
          :total="total"
          background
        />
      </div>
      <div class="upload-container">
        <FileUploadButton :baseId="props.id" :baseName="props.name" :empty="total === 0" />
      </div>
    </template>
  </ExpandWindow>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { assets } from '@/assets/assets'
import FileCard from './FileCard.vue'
import FileService from '@/service/FileService'
import { ElNotification, ElPagination } from 'element-plus'
import ExpandWindow from '@/components/FileUpload/ExpandWindow.vue'

import { useI18n } from 'vue-i18n'
import FileUploadButton from './FileUploadButton.vue'
const { t } = useI18n()

const props = defineProps({
  id: Number,
  name: String,
  type: String,
})

const visiable = ref(false)
// 存储所有文件数据（本地）
const allFiles = ref([])
// 当前页显示的文件列表
const files = ref([])
// 总文件数
const total = ref(0)
// 当前页码，默认为第一页
const currentPage = ref(1)
// 每页显示的文件数量
const pageSize = ref(4)

const iconUrl = computed(() => {
  const iconName = `${props.type}_icon`
  return assets[iconName] || assets.star_icon
})

const title = props.title || 'Error'
const description = props.description || 'Engineer is great science'

onMounted(async () => {
  try {
    const res = await FileService.getBaseFiles({
      baseNum: props.id,
      page: 1,
      pageSize: 1000, // 获取全部文件
    })
    allFiles.value = res.list
    total.value = res.list.length
    updateCurrentPageData()
  } catch (error) {
    ElNotification.error({
      message: '获取知识库文件失败',
    })
  }
})

const handleClick = () => {
  refresh()
  visiable.value = true
}

// 根据当前页码更新显示的数据
const updateCurrentPageData = () => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  files.value = allFiles.value.slice(startIndex, endIndex)
}

// 刷新数据（重新获取全部文件）
const refresh = async () => {
  try {
    const res = await FileService.getBaseFiles({
      baseNum: props.id,
      page: 1,
      pageSize: 1000, // 获取全部文件
    })
    allFiles.value = res.list
    total.value = res.list.length
    updateCurrentPageData()
  } catch (error) {
    ElNotification.error({
      message: '获取知识库文件失败',
    })
  }
}

// 处理当前页码改变事件
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  updateCurrentPageData()
}
</script>

<style scoped>
.wolfram-card {
  display: flex;
  align-items: center;
  max-width: 400px;
  width: 24vw;
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition:
    transform 0.2s ease-in-out,
    box-shadow 0.2s ease-in-out;
}
body.dark .wolfram-card {
  background-color: rgba(44, 44, 44, var(--opacity));
  border-color: #444;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.wolfram-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.wolfram-card__icon-wrapper {
  flex-shrink: 0;
  margin-right: 16px;
}

.wolfram-card__icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: contain;
  background-color: #f0f0f0;
}
body.dark .wolfram-card__icon {
  background-color: #3c3c3c;
}

.wolfram-card__content {
  flex-grow: 1;
}

.wolfram-card__title {
  margin: 0 0 4px 0;
  font-size: 1.2em;
  color: #333;
  font-weight: 600;
}
body.dark .wolfram-card__title {
  color: #c7c7c7;
}

.wolfram-card__description {
  margin: 0 0 8px 0;
  font-size: 0.9em;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2; /* 添加标准属性 */
  -webkit-box-orient: vertical;
}

.file-cards-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  padding: 20px 0;
}
.file-cards-grid.single-item-center {
  gap: 0px;
  grid-template-columns: repeat(1, 1fr);
  justify-items: center;
}
.file-card {
  max-width: 220px;
}

.empty-file-box{
  width:400px;
  height: 0px;
}

.file-cards-pagination-controls-container {
  margin-top: 0px;
  width: 100%;
  display: flex;
  justify-content: center;
}
.upload-container{
  margin-top: 15px;
}
.filepond--drop-label {
    color: #851616;
}
</style>
