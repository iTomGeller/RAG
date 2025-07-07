<template>
  <div class="dialog-container">
    <!-- <div v-if="!isOpen" class="file-up-load-button" @click="isOpen = !isOpen">
      {{ t('avatarUpload.title') }}
    </div> -->
    <!-- FilePond 文件上传区域 -->
    <file-pond
      ref="pondRef"
      name="file"
      :label-idle="props.empty ? t('fileUpload.empty') : t('fileUpload.upload')"
      max-files="8"
      allow-multiple="false"
      allow-revert="false"
      :file-validate-type-detect-type="customTypeDetector"
      accepted-file-types="text/plain, md/xlsx/xls, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf"
      :fileValidateTypeLabelExpectedTypes="t('fileUpload.invalidate')"
      instant-upload="false"
      :server="serverOptions"
      @processfile="handleUploadSuccess"
      :max-file-size="'10MB'"
    />
  </div>
</template>

<script setup>
import { assets } from '@/assets/assets'
import { onMounted, ref } from 'vue'
import { ElNotification } from 'element-plus'
import 'filepond/dist/filepond.min.css'
import vueFilePond from 'vue-filepond'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'

import { useI18n } from 'vue-i18n'
const { t } = useI18n()

import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type'
import baseURL from '@/config/baseURL.js'
const isOpen = ref(false)

const props = defineProps({
  baseId: {
    type: Number,
    required: true,
  },
  baseName: {
    type: String,
    required: true,
  },
  empty: {
    type: Boolean,
    default: false,
  },
})
//filepond自定义限制文件类型
const customTypeDetector = (source, type) => {
  return new Promise(function (resolve) {
    // 通过文件扩展名检测类型
    const extension = source.name.split('.').pop().toLowerCase()
    if (extension === 'xls' || extension === 'xlsx' || extension === 'md') {
      resolve('md/xlsx/xls')
    } else {
      resolve(type)
    }
  })
}
// 注册插件并创建 FilePond 组件
const FilePond = vueFilePond(FilePondPluginFileValidateSize, FilePondPluginFileValidateType)
const token = localStorage.getItem('token')
const emit = defineEmits(['update:uploaded'])
// 当前选择的文件列表
const pondRef = ref(null)
const title = t('knowledgebase.upload')

// 服务器上传配置
const serverOptions = {
  process: {
    url: `${baseURL}/files/upload/${props.baseId}`,
    method: 'POST',
    timeout: 30000,
    withCredentials: true,
    headers: {
      Authorization: `Bearer ${token}`, // 如果需要认证
    },
    onload: (response) => {
      console.log('response', response)
      // 返回的 id 会赋值给 file.serverId
      const res = JSON.parse(response)
      ElNotification.success({
        message: `文件${res.data.name}上传成功`,
      })
      return res.data.id
    },
    onerror: (response) => {
      console.error('上传失败返回值：', response)
      return '上传失败'
    },
  },
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
</script>

<style scoped>
/* .dialog-container {
} */
.file-up-load-button {
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 120px;
  height: 40px;
  border-radius: 10px;
  background-color: #67c23a;
}
</style>
