<template>
  <el-drawer
    :model-value="visible"
    @update:model-value="(val) => emit('update:visible', val)"
    :title="t('feedback.title')"
    direction="rtl"
    custom-class="feedback-drawer-class"
    size="30%"
  >
    <div class="feedback-form">
      <el-form @submit.prevent label-position="top">
        <el-form-item :label="t('feedback.content')">
          <el-input
            type="textarea"
            v-model="feedbackText"
            :placeholder="t('feedback.placeholder')"
            :autosize="{ minRows: 10, maxRows: 20 }"
          />
        </el-form-item>
        <!-- <el-form-item > -->
        <div class="btn-group">
          <el-button @click="submitFeedback" class="btn btn-primary">{{
            t('feedback.submit')
          }}</el-button>
          <el-button @click="closeDrawer" class="btn btn-secondary">{{
            t('feedback.cancel')
          }}</el-button>
        </div>
        <!-- </el-form-item> -->
      </el-form>
    </div>

    <el-divider></el-divider>

    <!-- 反馈组件 -->
    <div class="recent-feedback">
      <div class="recent-feedback-item" v-for="(item, index) in recentFeedback" :key="index">
        <el-card style="width: 100%; margin-bottom: 5px" shadow="hover">
          <div class="content">
            <!-- <div>{{ item.id }}</div> -->
            <div>{{ item.content }}</div>
            <el-button type="text" @click="deleteFeedback(item.id)"
              ><el-icon><Delete /></el-icon
            ></el-button>
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
  </el-drawer>
</template>

<script setup>
import { ref,onMounted, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import FeedbackService from '@/service/FeedbackService'
import { ElMessage,ElNotification } from 'element-plus'

const { t } = useI18n()

const recentFeedback = ref([])
const currentPage = ref(1)
const pageSize = ref(3)
const total = ref(0)


onMounted(async () => {
  await refresh()
})


const props = defineProps({
  visible: Boolean,
})

const emit = defineEmits(['update:visible'])

const feedbackText = ref('')

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


const submitFeedback = async () => {
  if (!feedbackText.value.trim()) {
    ElMessage.warning(t('feedback.emptyWarning'))
    return
  }

  try {
    await FeedbackService.addFeedback(feedbackText.value)
    ElMessage.success(t('feedback.success'))
    console.log('用户反馈内容：', feedbackText.value)
    feedbackText.value = ''
    emit('update:visible', false)
  } catch (e) {
    console.error('反馈提交失败:', e)
    ElMessage.error(t('feedback.error'))
  }
}

// 关闭抽屉
const closeDrawer = () => {
  emit('update:visible', false)
}


watch(
  () => props.visible,
  (val) => {
    if (val) {
      refresh()
    }
  },
)


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

.feedback-form {
  padding: 16px;
}

.btn-group {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.btn-group .btn {
  border-radius: 10px;
  color: #282828;
  cursor: pointer;
}

.btn:hover {
  background-color: #d5d8dc;
}

.btn-primary {
  background-color: #e2e6eb;
}

.btn-secondary {
  background-color: #ffffff;
}

.file-cards-pagination-controls-container{
  display: flex;
  justify-content: center;

}
</style>
