<template>
  <div class="main-container">
    <div class="main">
      <div class="featured-section">
        <h1>{{ t('knowledgebase.title') }}</h1>
        <p>{{ t('knowledgebase.description1') }}</p>
        <p>{{ t('knowledgebase.description2') }}</p>
      </div>

      <div class="file-boxs-grid" :class="{ 'single-item-center': boxList.length === 1 }">
        <transition-group name="fade-slide" mode="out-in">
          <div v-for="box in boxList" :key="box.id" class="file-box-wrapper">
            <FileBox
              :id="box.id"
              :name="$t('knowledgebase.categories.' + box.name, box.name)"
              :type="box.type"
            />
          </div>
        </transition-group>
      </div>

      <div class="pagination-controls-container">
        <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="1"
          :layout="paginationLayout"
          :total="totalBoxes"
          background
        >
        </el-pagination>
      </div>

      <div class="btnGroup">
        <FileUpload @update:uploaded="handleFileUploaded" @cancel="showUpload = false" />

        <AddBaseBtn @add-base-success="refresh" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

import FileBox from '@/components/FileUpload/FileBox.vue'
import FileUpload from '@/components/FileUpload/FileUpload.vue'
import { ElPagination, ElNotification } from 'element-plus'

import BaseService from '@/service/BaseService'

import { useI18n } from 'vue-i18n'
import AddBaseBtn from '@/components/Base/AddBaseBtn.vue'
const { t } = useI18n()

// 所有知识库数据（本地存储）
const allBoxes = ref([])
// 当前页显示的知识库列表
const boxList = ref([])
// 总数用于分页
const totalBoxes = ref(0)
// 当前页码，默认为第一页
const currentPage = ref(1)
// 每页显示的文件数量
const pageSize = ref(4)

// Element Plus 分页组件的布局
const paginationLayout = ref('prev, pager, next') // 只显示上一页、页码和下一页

// 在 mounted 时一次性获取所有数据
onMounted(async () => {
  refresh()
})

// 根据当前页码更新显示的数据
const updateCurrentPageData = () => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  boxList.value = allBoxes.value.slice(startIndex, endIndex)
}

// 刷新数据（仅更新当前页）
const refresh = async () => {
  try {
    const res = await BaseService.getUserBaseInfo({ page: 1, pageSize: 100 }) // 获取全部数据
    allBoxes.value = res.list
    totalBoxes.value = res.list.length
    updateCurrentPageData()
  } catch (error) {
    ElNotification.error({
      message: error.message,
    })
  }
}

// 处理当前页码改变事件
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  updateCurrentPageData()
}

// 处理每页显示数量改变事件 (这里pageSize是固定值，但回调函数依然保留)
const handleSizeChange = (newSize) => {
  console.log('每页显示数量改变为:', newSize)
}
</script>

<style scoped>
.main-container {
  height: 100vh;
  width: 100%;
  padding-left: 20%;
  padding-right: 20%;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  text-align: center;
}
.main {
  display: flex;
  flex-direction: column;
  align-items: center;
}

h1 {
  /* 假设是h1 */
  font-size: 2.5em;
  /* 调整字体大小 */
  color: #333;
  margin-bottom: 10px;
}
body.dark .main h1 {
  color: #c7c7c7;
  /* 深色模式下的标题颜色 */
}

.featured-section {
  margin-top: 30px;
  margin-bottom: 20px;
  width: 100%;
  /* 确保占据整个宽度 */
}

.featured-section p {
  font-size: 1.1em;
  color: #666;
  line-height: 1.5;
}

.file-boxs-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  padding: 20px 0;
  max-width: 600px;
  /* 假设 FileBox 宽度 approx 280px * 2 + 30px gap */
  width: 100%;
  /* 确保在 max-width 范围内占据可用宽度 */
  justify-content: center;
  /* 如果列数不足，内容居中 */
  min-height: 230px;
}

.pagination-controls-container {
  margin-top: 30px;
  /* 与文件网格的间距 */
  margin-bottom: 30px;
  /* 与下方按钮的间距 */
  width: 100%;
  display: flex;
  justify-content: center;
  /* 使分页组件本身居中 */
}

/* 覆盖 Element Plus 默认样式，如果需要 */
.el-pagination {
  /* 例如，调整分页组件的字体大小 */
  font-size: 14px;
}

/* 如果你需要 Element Plus 文档中的 example-pagination-block 样式，
   可以将其复制过来或直接应用在 pagination-controls-container 上 */
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}

.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}

.file-boxs-grid.single-item-center {
  grid-template-columns: 1fr;
  justify-items: center;
}

.btnGroup {
  display: flex;
  gap: 30px;
}
.fade-slide-enter-active {
  transition: all 0.4s ease;
  opacity: 0;
  transform: translateY(20px);
}

.fade-slide-enter-to {
  opacity: 1;
  transform: translateY(0);
}

.fade-slide-leave-active {
  position: absolute;
  opacity: 0;
  transform: translateY(-20px);
  transition: all 0s ease;
}
</style>
