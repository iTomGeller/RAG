<template>
  <div class="file-upload-container">
    <!-- FilePond 文件上传区域 -->
    <file-pond
      ref="pond"
      name="file"
      label-idle="拖拽文件到这里或点击上传"
      allow-multiple
      accepted-file-types="file/*"
      :max-file-size="'10MB'"
      :instant-upload="false"
      :server="{}"
      @addfile="handleAddFile"
      @removefile="handleRemoveFile"
    />

    <!-- 操作按钮 -->
    <div class="actions">
      <el-button @click="uploadFiles" :loading="isUploading" type="primary">{{
        isUploading ? '' : '确认上传'
      }}</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import vueFilePond from 'vue-filepond'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'
import FileService from '@/service/FileService'

// 注册插件并创建 FilePond 组件
const FilePond = vueFilePond(FilePondPluginFileValidateSize)

// 定义 props 接收 baseId
const props = defineProps({
  baseId: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['update:uploaded'])
// 当前选择的文件列表
const files = ref([])
const pond = ref(null)
const isUploading = ref(false);

// 添加文件到本地列表
const handleAddFile = (error, file) => {
  if (!error) {
    files.value.push(file)
  }
}

// 移除文件
const handleRemoveFile = (error, file) => {
  console.log('移除文件:', file)
  const index = files.value.findIndex((f) => f.id === file.id)
  if (index > -1) {
    files.value.splice(index, 1)
  }
}

// 确认上传所有文件
const uploadFiles = async () => {
  isUploading.value = true; // 开始加载动画
  for (const file of files.value) {
    try {
      // 创建 FormData 对象
      const formData = new FormData()
      // 将文件添加到 FormData 中，参数名为 'file'
      formData.append('file', file.file)
      // 调用 FileService.addFile 方法
      await FileService.addFile(props.baseId, formData)
      console.log(`文件 ${file.filename} 上传成功`)
    } catch (err) {
      console.error(`文件 ${file.filename} 上传失败`, err)
    }
  }
  // 清空文件列表
  files.value = []
  pond.value.removeFiles()
  emit('update:uploaded') // 触发事件并传递当前状态
  isUploading.value = false; // 结束加载动画
}
</script>

<style scoped>
.file-upload-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}
.remove-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: red;
}
.actions {
  margin-top: 20px;
  text-align: right;
}
</style>
