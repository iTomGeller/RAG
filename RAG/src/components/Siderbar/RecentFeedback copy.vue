<template>
  <!-- 最近反馈弹窗 -->
  <el-dialog
    :before-close="clickCancel"
    :visible.sync="isDialogVisible"
    title="最近反馈"
    width="50%"
    :close-on-click-modal="false"
  >
    <div class="recent-feedback">
      <div class="recent-feedback-item" v-for="(item, index) in recentFeedback" :key="index">
        <el-card style="width: 100%; margin-bottom: 10px" shadow="hover">
          <div class="content">
            <div>{{ item.id }}</div>
            <div>{{ item.content }}</div>
            <el-button type="text" @click="deleteFeedback(item.id)"><el-icon><Delete/></el-icon></el-button>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="file-cards-pagination-controls-container">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :layout="'prev, pager, next'"
        :total="total"
        background
      />
    </div>
  </el-dialog>
</template>

<script setup>
import FeedbackService from '@/service/FeedbackService'
import { ElNotification } from 'element-plus'
import { onMounted, ref } from 'vue'

const recentFeedback = ref([])

const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

onMounted(async () => {
  await refresh()
})


const refresh = async () => {
  try {
    const res = await FeedbackService.getRecentFeedbacks({
      page: currentPage.value,
      pageSize: pageSize.value,
    })
    recentFeedback.value = res.list
    total.value = res.total
  } catch (error) {
    ElNotification.error({
      title: '错误',
      message: '获取最近反馈失败',
    })
  }
}

defineExpose({
    refresh,
})


const handleCurrentChange = async (newPage) => {
  currentPage.value = newPage
  await refresh()
}

const deleteFeedback = async (id) => {
  try {
    await FeedbackService.deleteFeedback(id)
    ElNotification.success({
      title: '成功',
      message: '删除反馈成功',
    })
    await refresh()
  } catch (error) {
    ElNotification.error({
      title: '错误',
      message: '删除反馈失败',
    })
  }
}
</script>

<style scoped>
.recent-feedback {
  width: 100%;
}

.content{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}
</style>
