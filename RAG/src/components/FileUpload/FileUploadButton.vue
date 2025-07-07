<template>
  <img
    :src="assets.folder_icon"
    @click="dialog = true"
    alt="Gallery Icon"
    style="cursor: pointer; width: 30px; height: 30px"
  />

  <el-dialog
    :before-close="clickCancel"
    v-model="dialog"
    title="上传文件"
    size="80%"
    :with-header="false"
  >
    <div class="upload-container">
      <file-pond
        ref="pondRef"
        name="file"
        label-idle="拖拽文件到这里或点击上传(文件小于10MB)"
        allow-multiple="false"
        accepted-file-types="file/*"
        :server="serverOptions"
        :instant-upload="true"
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
import { ref , defineProps, onMounted} from 'vue'
import vueFilePond from 'vue-filepond'
import 'filepond/dist/filepond.min.css'
import { ElMessage } from 'element-plus'
import baseURL from '@/config/baseURL.js'
import FileService from '@/service/FileService.js'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'

const props = defineProps({
  BaseId: Number,
})

onMounted(
  ()=>{
    console.log("BaseId",props.BaseId)
  }
)


const FilePond = vueFilePond(FilePondPluginFileValidateSize)
const token = localStorage.getItem('token')

// 文件相关配置
const fileLink = ref('')
const fileName = ref('')
const fileSize = ref('')
const fileId = ref('')
const dialog = ref(false)
const pondRef = ref(null)

const clear = () => {
  fileLink.value = ''
  fileName.value = ''
  fileSize.value = ''
}

const close = () => {
  clear()
  dialog.value = false
}

// 服务器上传配置
const serverOptions = {
  process: {
    url: `${baseURL}/files/upload/${props.BaseId}`, 
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
      console.log('res.data.url', res.data.url)
      console.log('res.data.id', res.data.id)
      fileId.value = res.data.id
      return res.data.id
    },
    onerror: (response) => {
      console.error('上传失败返回值：', response)
      return '上传失败'
    },
  },
  revert: (serverId, load, error) => {
    console.log('revert serverId', serverId)
    fetch(`${baseURL}/files`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ id: serverId }), // 后端要求的是JSO(url:"http……")而不是string
    })
      .then((res) => {
        if (res.ok) {
          load() // 通知 FilePond 删除成功
        } else {
          return res.text().then((text) => {
            throw new Error(text)
          })
        }
      })
      .catch((err) => {
        console.error('删除失败', err)
        error('删除失败')
      })
  },
}

const handleCheck = async () => {
  close()
}

const deleteFile = async () => {
  console.log('后端不暴露delete接口,此功能暂时没有实现')
  close()
  return
  // 后端暂时没有实现

  console.log('deleteFile fileId', fileId.value)
  try {
    await FileService.deleteFile(fileId.value)
  } catch (error) {
    ElMessage.error('删除失败', error)
  }
  close()
}

// 处理上传成功事件
const handleUploadSuccess = (error, file) => {
  if (!error) {
    ElMessage.success('上传成功')

    // 得到原始文件名
    const originalFileName = file.filename // 原始文件名

    fileName.value = originalFileName
    fileLink.value = file.serverId
    const fileSizeBytes = file.fileSize // FilePond 自动提供的大小（字节数）
    const sizeFormatted = formatFileSize(fileSizeBytes)

    fileSize.value = sizeFormatted

    console.log('原始文件名:', originalFileName)
    console.log('Url', file.serverId)
    console.log('文件大小:', sizeFormatted) // 示例：1.3 MB

    // 七秒之后删除小弹窗
    setTimeout(() => {
      pondRef.value?.removeFile(file.id)
    }, 7000)
  } else {
    ElMessage.error('上传失败', error)
  }
}

function formatFileSize(bytes) {
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / 1024 / 1024).toFixed(1)} MB`
}
</script>

<style>
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
