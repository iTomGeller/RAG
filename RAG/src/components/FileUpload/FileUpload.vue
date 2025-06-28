<template>
  <div class="file-upload-card" @click="handleClick">
    <div class="file-upload-card__icon-wrapper">
      <img :src="iconUrl" :alt="title + ' Icon'" class="file-upload-card__icon" />
    </div>
    <div class="file-upload-card__content">
      <h3 class="file-upload-card__title">{{ title }}</h3>
    </div>
  </div>
  <el-dialog
    :before-close="clickCancel"
    v-model="visiable"
    size="80%"
    title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;放入文件，我会帮你自动分类..."
    :with-header="false"
  >
    <div class="file-upload-container">
      <!-- FilePond 文件上传区域 -->
      <file-pond
        ref="pond"
        name="file"
        label-idle="拖拽文件到这里或点击上传(文件小于10MB)"
        allow-multiple
        accepted-file-types="['text/plain', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'text/markdown', 'application/pdf']"
        :max-file-size="'10MB'"
        :instant-upload="false"
        :server="{}"
        @addfile="handleAddFile"
        @removefile="handleRemoveFile"
      />

      <div class="file-upload-result-container">
        <el-button @click="uploadFiles" :loading="isUploading" class="button">
          {{ isUploading ? '' : '确认上传' }}
        </el-button>
        <el-button @click="onCancel" class="button">
          {{ '取消' }}
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { assets } from '@/assets/assets'
import { onMounted, ref } from 'vue'
import { ElNotification } from 'element-plus'
import vueFilePond from 'vue-filepond'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'
import FileService from '@/service/FileService'
import BaseService from '@/service/BaseService'

// 注册插件并创建 FilePond 组件
const FilePond = vueFilePond(FilePondPluginFileValidateSize)

const emit = defineEmits(['update:uploaded'])
// 当前选择的文件列表
const files = ref([])
const pond = ref(null)
const baseInfo = ref([])
const isUploading = ref(false)
//显示相关
const iconUrl = assets.new_box_icon
const title = '上传文件到您的知识库'
const visiable = ref(false)
const handleClick = () => {
  visiable.value = true
}
const onCancel = () => {
  visiable.value = false
  clear()
}
// 添加文件到本地列表
const handleAddFile = (error, file) => {
  if (!error) {
    files.value.push(file)
  }
}

// 移除文件
const handleRemoveFile = (error, file) => {
  if (!error) {
    console.log('移除文件:', file)
    const index = files.value.findIndex((f) => f.id === file.id)
    if (index > -1) {
      files.value.splice(index, 1)
    }
  }
}
// 确认上传所有文件
const uploadFiles = async () => {
  console.log(baseInfo.value)
  isUploading.value = true // 开始加载动画
  const filesTemp = files.value.slice()
  for (const file of filesTemp) {
    try {
      // 创建 FormData 对象
      const formData = new FormData()
      // 将文件添加到 FormData 中，参数名为 'file'
      formData.append('file', file.file)
      // 调用 FileService.addFile 方法
      const res = await FileService.addFile(formData)
      console.log(`文件 ${res.data.name} 上传成功`)
      //显示分类信息
      fileDestination(res.data.name, res.data.knowledgeBaseId)
    } catch (err) {
      console.error(`文件 ${file.filename} 上传失败`, err)
    } finally {
      //移除filepond中的对应文件
      pond.value.removeFile(file)
    }
  }
  clear()
  emit('update:uploaded') // 触发事件并传递当前状态
  onCancel()
}
//文件分类信息
const fileDestination = async (name, id) => {
  ElNotification.success({
    message: `文件“ ${name} ”上传成功,\n已被放入 ${baseInfo.value.find((item) => item.id === id).name}`,
  })
}
//取消
const clear = () => {
  // 清空文件列表
  files.value = []
  pond.value.removeFiles()
  isUploading.value = false // 结束加载动画
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
const clickCancel = (done) => {
  visiable.value = false
  clear()
  done()
}
</script>

<style scoped>
.file-upload-container {
  width: 400px;
  margin: 20px auto;
}
.remove-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: red;
}
.button {
  width: 30%;
}
.file-upload-card {
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
.file-upload-card__icon-wrapper {
  flex-shrink: 0;
  margin-right: 16px;
}

.file-upload-card__icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: contain;
  background-color: #f0f0f0;
}
.file-upload-card__content {
  flex-grow: 1;
}
.file-upload-card__title {
  margin: 0 0 4px 0;
  font-size: 1.2em;
  color: #333;
  font-weight: 600;
}
.file-upload-result-container {
  width: auto;
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 10%;
}
</style>
