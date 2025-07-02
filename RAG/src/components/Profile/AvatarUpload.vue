<template>
  <el-button  @click="dialog = true" alt="Logout Icon" class="act-btn"
    > {{ t('profile.edit') }}</el-button>

  <el-dialog
    v-model="dialog"
    title="上传文件"
    size="80%"
    :with-header="false"
  >
    <div class="upload-container">
      <file-pond
        ref="pondRef"
        name="file"
        label-idle="拖拽图片到这里或点击上传(图片小于10MB)"
        allow-multiple="false"
        accepted-file-types="image/jpeg, image/png"
        :server="serverOptions"
        :instant-upload="true"
        @processfile="handleUploadSuccess"
        :max-file-size="'10MB'"
      />
      <div v-if="fileLink != null" class="result-container">
        <el-button @click="confirmUpdate" class="button">
          {{ '确认' }}
        </el-button>
        <el-button @click="onCancel" class="button">
          {{ '取消' }}
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref} from 'vue'

import { useI18n } from 'vue-i18n'
const { t } = useI18n()

import vueFilePond from 'vue-filepond'
import 'filepond/dist/filepond.min.css'
import { ElMessage } from 'element-plus'
import baseURL from '@/config/baseURL.js'
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'
import axios from 'axios'

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
    url: `${baseURL}/files/upload/avatar`, // 此处的数字对应知识库id
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

      console.log('res.data', res.data)
      fileId.value = res.data
      return res.data
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




const confirmUpdate = async () => {
  if (fileLink.value === '') {
    ElMessage.error('请先上传文件')
    return
  }
  try {
    const response = await axios.put(
      `${baseURL}/user/update`,
      { avatarUrl: fileLink.value },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      },
    )

    if (response.status === 200) {
      ElMessage.success('头像更新成功')

      // 更新本地 userInfo 信息中的 avatarUrl
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      userInfo.avatarUrl = fileLink.value
      localStorage.setItem('userInfo', JSON.stringify(userInfo))

      location.reload()

      // close()
    } else {
      ElMessage.error('头像更新失败')
    }
  } catch (error) {
    console.error('更新用户头像出错:', error)
    ElMessage.error('头像更新请求失败')
  }
}

const onCancel = () => {
  close()
}
</script>

<style scoped>
.upload-container {
  width: 400px;
  margin: 20px auto;
  transition:opacity 0.3s ease;
}

.result-container {
  width: auto;
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 10%;
  transition:opacity 0.3s ease;
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
  transition:opacity 0.3s ease;
}

.button {
  width: 30%;
}

.act-btn {
  height: 50px;
  width: 200px;
  margin: 0 0 0 5px !important;
  padding: 0 !important;
  background-color: transparent;
  border: none;
  border-radius: 20px;
}
</style>
