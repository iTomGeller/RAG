<template>
  <div class="wolfram-card" @click="handleClick">
    <div class="wolfram-card__icon-wrapper">
      <img :src="iconUrl" :alt="text.title + ' Icon'" class="wolfram-card__icon" />
    </div>
    <div class="wolfram-card__content">
      <h3 class="wolfram-card__title">{{ text.title }}</h3>
    </div>
  </div>

  <el-dialog :before-close="handleCancel" v-model="visiable" :title="text.title" size="80%" :with-header="false">
    <el-form label-width="auto" class="form" >

      <el-form-item :label="text.baseName" >
        <el-tooltip class="box-item" effect="dark" :content="noteLabelContent" placement="right-end">
          <el-input style="width:80%" v-model="baseName" />
        </el-tooltip>
      </el-form-item>

      <!-- 不再使用type -->
      <!-- <el-form-item :label="text.baseType">
        <el-select v-model="baseType" placeholder="" size="large" style="width: 240px">
          <el-option
            v-for="item in baseTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item> -->

      <div class="button-group">
        <el-button class="button" @click="handleCancel">{{ text.cancel }}</el-button>
        <el-button class="button" @click="handleCheck">{{ text.check }}</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>


<script setup>
import { assets } from '@/assets/assets';
import { onMounted, ref, watchEffect, computed } from 'vue';
import BaseService from '@/service/BaseService';
import { ElNotification } from 'element-plus';

const iconUrl = assets.new_box_icon

// 语言状态
const currentLang = ref(localStorage.getItem('language') || 'en')


// 国际化文本定义
const i18nText = {
  en: {
    title: 'create Knowledge Base',
    baseName: 'Base Name',
    baseType: 'Base Type',
    check: 'Check',
    cancel: 'Cancel'
  },
  zh: {
    title: '新建知识库',
    baseName: '知识库名称',
    baseType: '知识库类型',
    check: '提交',
    cancel: '取消'
  }
}

const text = computed(() => i18nText[currentLang.value])

const noteLabelContent= ref("")

const noteLabel = {
  en: 'This name will be used as a basis for AI classification.',
  zh: '此名称将作为AI分类基础'
}

// 中英 baseType 选项静态定义
const baseTypeMap = {
  en: [
    { value: 'Default', label: 'Default' },
    { value: 'Humanities', label: 'Humanities' },
    { value: 'Science', label: 'Science' },
    { value: 'Engineering', label: 'Engineering' },
    { value: 'Health', label: 'Health' },
    { value: 'Social', label: 'Social' }
  ],
  zh: [
    { value: 'Default', label: '默认' },
    { value: 'Humanities', label: '人文' },
    { value: 'Science', label: '理科' },
    { value: 'Engineering', label: '工科' },
    { value: 'Health', label: '医学' },
    { value: 'Social', label: '社科' }
  ]
}

const baseTypeOptions = ref(baseTypeMap[currentLang.value])

// 监听语言切换
watchEffect(() => {
  const lang = localStorage.getItem('language') || 'en'
  currentLang.value = lang
  baseTypeOptions.value = baseTypeMap[lang]
  noteLabelContent.value = noteLabel[lang]
})

// 控制弹窗
const visiable = ref(false)
const baseName = ref("")
const baseType = ref()
const emit = defineEmits(['addBaseSuccess'])

const clear = () => {
  baseName.value = ""
  baseType.value = ""
}

const close = () => {
  visiable.value = false;
  clear()
}

const handleClick = () => {
  visiable.value = true;
}

const handleCheck = async () => {
  if (!baseName.value || !baseType.value) {
    ElNotification.error({ message: currentLang.value === 'zh' ? '请填写所有字段' : 'Please fill in all fields' })
    return
  }

  try {
    await BaseService.addBase({
      name: baseName.value,
      type: baseType.value
    })
    ElNotification.success({
      message: currentLang.value === 'zh' ? '知识库创建成功' : 'Create knowledge base successfully'
    })
    emit('addBaseSuccess')
    close()
  } catch (error) {
    ElNotification.error({
      message: currentLang.value === 'zh' ? '知识库创建失败' : 'Create knowledge base failed'
    })
  }
}

const handleCancel = () => {
  visiable.value = false
  close()
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
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
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

.form {
  padding: 5%;
  max-width: 60vw;
}

.button-group {
  width: auto;
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 5%;
  transition:opacity 0.3s ease;
}

.button{
  width: 15%;
}
</style>