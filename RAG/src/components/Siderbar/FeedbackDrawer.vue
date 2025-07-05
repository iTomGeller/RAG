<template>
  <el-drawer
    :model-value="visible"
    @update:model-value="val => emit('update:visible', val)"
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
            :autosize="{ minRows: 15, maxRows: 20 }"
          />
        </el-form-item>
        <!-- <el-form-item > -->
          <div class = 'btn-group'>
          <el-button @click="submitFeedback" class="btn btn-primary" >{{ t('feedback.submit') }}</el-button>
          <el-button @click="closeDrawer" class="btn btn-secondary">{{ t('feedback.cancel') }}</el-button>
          </div>  
        <!-- </el-form-item> -->
      </el-form>
    </div>

    <el-divider></el-divider>

    <el-button @click="handleClick">最近反馈</el-button>
    <RecentFeedback v-model="isDialogVisible" ref="feedbackRef"/>
    
  </el-drawer>
</template>

<script setup>
import { nextTick, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import FeedbackService from '@/service/FeedbackService';
import RecentFeedback from './RecentFeedback.vue';
import { ElMessage } from 'element-plus';

const { t } = useI18n();
const props = defineProps({
  visible: Boolean
});
const emit = defineEmits(['update:visible']);

const feedbackText = ref('');

const isDialogVisible = ref(false);

const feedbackRef = ref(null);//instance of RecentFeedback


const handleClick = async() => {
  isDialogVisible.value = true;

  await nextTick();
  
  if (feedbackRef.value){
    feedbackRef.value.refresh();
  }
}


const submitFeedback = async () => {
  if (!feedbackText.value.trim()) {
    ElMessage.warning(t('feedback.emptyWarning'));
    return;
  }

  try {
    await FeedbackService.addFeedback(feedbackText.value);
    ElMessage.success(t('feedback.success'));
    console.log('用户反馈内容：', feedbackText.value);
    feedbackText.value = '';
    emit('update:visible', false);
  } catch (e) {
    console.error('反馈提交失败:', e);
    ElMessage.error(t('feedback.error'));
  }
};

// 关闭抽屉
const closeDrawer = () => {
  emit('update:visible', false);
};
</script>

<style scoped>
.feedback-form {
  padding: 16px;
}

.btn-group {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  }

.btn-group .btn{
  border-radius: 10px;
  color: #282828;
  /* background-color: #e2e6eb; */
  cursor: pointer;
}

.btn:hover{
    background-color: #d5d8dc;
}

.btn-primary{
  background-color: #e2e6eb;
    }

.btn-secondary{
    background-color: #ffffff;
    }
</style>
