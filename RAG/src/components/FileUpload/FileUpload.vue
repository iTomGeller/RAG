<template>
  <div class="wolfram-card" @click="dialog = true">
    <div class="wolfram-card__icon-wrapper">
      <img :src="iconUrl" :alt="title + ' Icon'" class="wolfram-card__icon" />
    </div>
    <div class="wolfram-card__content">
      <h3 class="wolfram-card__title">{{ title }}</h3>
    </div>
  </div>
  <el-dialog
    :before-close="clickCancel"
    v-model="dialog"
    size="80%"
    title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;放入文件，我会帮你自动分类..."
    :with-header="false"
  >
    <div class="upload-container">
      <!-- FilePond 文件上传区域 -->
      <file-pond
        ref="pondRef"
        name="file"
        label-idle="拖放文档文件到这里<br/><span class='file-types'>支持格式: txt, doc, docx, md, pdf, xls, xlsx</span>"
        max-files="8"
        allow-multiple="false"
        allow-revert="false"
        accepted-file-types="text/plain, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, text/markdown, application/pdf"
        fileValidateTypeLabelExpectedTypes="文件类型不符合要求"
        instant-upload="false"
        :server="serverOptions"
        @processfile="handleUploadSuccess"
        :max-file-size="'10MB'"
      />

      <div v-if="fileLink != null" class="result-container">
        <el-button @click="handleCheck" class="button">
          {{ '确认' }}
        </el-button>
        <el-button @click="deleteFile" class="button">
          {{ '撤销' }}
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { assets } from '@/assets/assets'
import { onMounted, ref } from 'vue'
import { ElNotification } from 'element-plus'
import 'filepond/dist/filepond.min.css'
import vueFilePond from 'vue-filepond'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'
import BaseService from '@/service/BaseService'

import { useI18n } from 'vue-i18n'
const { t } = useI18n()

import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type'
import baseURL from '@/config/baseURL.js'

// 注册插件并创建 FilePond 组件
const FilePond = vueFilePond(FilePondPluginFileValidateSize, FilePondPluginFileValidateType)
const token = localStorage.getItem('token')
const emit = defineEmits(['update:uploaded'])
// 当前选择的文件列表
const pondRef = ref(null)
//提前获得知识库信息
const baseInfo = ref([])
//样式语言规定
const iconUrl = assets.new_box_icon

const title = t('knowledgebase.upload')
const dialog = ref(false)


// const visiable = ref(false)
// const handleClick = () => {
//   visiable.value = true
// }
// const onCancel = () => {
//   visiable.value = false
//   clear()
// }
// // 添加文件到本地列表
// const handleAddFile = (error, file) => {
//   if (!error) {
//     files.value.push(file)
//   }
// }
// const title = '上传文件到您的知识库'


const close = () => {
  dialog.value = false
}
// 服务器上传配置
const serverOptions = {
  process: {
    url: `${baseURL}/files/upload/auto-classify`, // 此处的数字对应知识库id
    method: 'POST',
    timeout: 7000,
    withCredentials: true,
    headers: {
      Authorization: `Bearer ${token}`, // 如果需要认证
    },
    onload: (response) => {
      console.log('response', response)
      // 返回的 id 会赋值给 file.serverId
      const res = JSON.parse(response)
      fileDestination(res.data.name, res.data.knowledgeBaseId)
      return res.data.id
    },
    onerror: (response) => {
      console.error('上传失败返回值：', response)
      return '上传失败'
    },
  },
}
const handleCheck = async () => {
  close()
}
// 处理上传成功事件
const handleUploadSuccess = (error, file) => {
  if (!error) {
    emit('update:uploaded') // 触发事件并传递当前状态
    // 七秒之后删除小弹窗
    setTimeout(() => {
      pondRef.value?.removeFile(file.id)
    }, 7000)
  } else {
    ElNotification.error('上传失败', error)
  }
}
//文件分类信息
const fileDestination = async (name, id) => {
  ElNotification.success({
    message: `文件“ ${name} ”上传成功,\n已被放入 ${baseInfo.value.find((item) => item.id === id).name}`,
  })
}
onMounted(async () => {
  // 获得知识库列表\
  try {
    const res = await BaseService.getUserBaseInfo({ page: 1, pageSize: 1000 })
    baseInfo.value = res.list
  } catch (error) {
    console.log(error)
    ElNotification.error({
      message: '获取知识库列表失败',
    })
  }
})
</script>

<style scoped>
.wolfram-card {
  display: flex;
  align-items: center;
  width: 400px;
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

.wolfram-card__content {
  flex-grow: 1;
}

.wolfram-card__title {
  margin: 0 0 4px 0;
  font-size: 1.2em;
  color: #333;
  font-weight: 600;
}

.upload-container {
  width: 400px;
  margin: 20px auto;
}

.result-container {
  width: auto;
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 10%;
}

.link-result {
  background-color: #f5f5f5;
  /* color: var(--paper-white); */
  border: 2px solid black;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
  cursor: pointer;
  font-family: Arial, serif;
  font-size: 18px;
  height: auto;
}

.file-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.button {
  width: 30%;
}
</style>
