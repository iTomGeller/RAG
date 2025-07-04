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
            :autosize="{ minRows: 5, maxRows: 10 }"
          />
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="submitFeedback">{{ t('feedback.submit') }}</el-button>
          <el-button @click="closeDrawer">{{ t('feedback.cancel') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-button @click="handleClick">最近反馈</el-button>
    <RecentFeedback v-model="isDialogVisible"/>
    
  </el-drawer>
</template>

<script setup>
import { ref } from 'vue';
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

const handleClick = () => {
  isDialogVisible.value = true;
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


</style>
