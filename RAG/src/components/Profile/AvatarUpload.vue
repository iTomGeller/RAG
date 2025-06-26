<template>
  <el-button @click="dialog = true" alt="Logout Icon" class="act-btn">修改信息</el-button>

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
        label-idle="拖拽图片到这里或点击上传(图片小于10MB)"
        allow-multiple="false"
        accepted-file-types="image/jpeg, image/png"
        :server="serverOptions"
        :instant-upload="true"
        @processfile="handleUploadSuccess"
        :max-file-size="'10MB'"
      />
      <div v-if="fileLink != null" class="result-container">
        <el-button @click="sendFile(fileLink, fileName, fileSize)" class="button">
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
import { ref, defineEmits } from 'vue'
import vueFilePond from 'vue-filepond'
import 'filepond/dist/filepond.min.css'
import { ElMessage } from 'element-plus'
import baseURL from '@/config/baseURL.js'
import axios from 'axios'


// 导入插件
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size'

// 注册插件
const FilePond = vueFilePond(FilePondPluginFileValidateSize)

// 创建 FilePond 组件
const token = localStorage.getItem('token')

// 文件相关配置
const fileLink = ref('')
const fileName = ref('')
const fileSize = ref('')

const dialog = ref(false)

// const props = defineProps(["roomId"]);
//通知父组件
const emit = defineEmits(['updateUserInfo']);

const pondRef = ref(null)

// 服务器上传配置
const serverOptions = {
  process: {
    url: `${baseURL}/files/upload/avatar`,
    method: 'POST',
    timeout: 7000,
    withCredentials: true,
    headers: {
      Authorization: `Bearer ${token}`, // 如果需要认证
    },
    onload: (response) => {
      console.log('response', response)
      // 返回的 URL 会赋值给 file.serverId
      const res = JSON.parse(response)
      return res.url
    },
    onerror: (response) => {
      console.error('上传失败返回值：', response)
      return '上传失败'
    },
  },

  // 用户取消上传时触发
  revert: (serverId, load, error) => {
    fetch(`${baseURL}/api/fileupload/delete`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ url: serverId }), // 后端要求的是JSO(url:"http……")而不是string
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

const sendFile = async (fileLink, fileName, fileSize) => {
  try {
    await signalRService.sendMessage(props.roomId, `${fileName}-(${fileSize})`, fileLink)
  } catch (error) {
    ElMessage.error('上传失败', error)
  }
  dialog.value = false
}

//取消按钮的点击事件
// const deleteFile = async (fileLink) => {
//   try {
//     await FileService.deleteFile(fileLink)
//   } catch (error) {
//     ElMessage.error('删除失败', error)
//   }
//   dialog.value = false
// }

// 处理上传成功事件
const handleUploadSuccess = (error, file) => {
  if (!error) {
    ElMessage.success('上传成功')

    // 得到原始文件名
    // const originalFileName = file.filename // 原始文件名
    // console.log('原始文件名:', originalFileName)
    // fileName.value = originalFileName
    fileName.value = file.serverId

    // // 得到最终返回文件链接
    // console.log('Url', file.serverId)
    // fileLink.value = file.serverId

    //  得到文件大小
    const fileSizeBytes = file.fileSize // FilePond 自动提供的大小（字节数）
    const sizeFormatted = formatFileSize(fileSizeBytes)
    fileSize.value = sizeFormatted

    console.log('文件大小:', sizeFormatted) // 示例：1.3 MB

    // 七秒之后删除小弹窗
    setTimeout(() => {
      pondRef.value?.removeFile(file.id)
    }, 7000)
  } else {
    ElMessage.error('上传失败', error)
  }
}

//文件大小格式化
function formatFileSize(bytes) {
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / 1024 / 1024).toFixed(1)} MB`
}


const confirmUpdate = async () => {
  if (!fileLink.value) {
    ElMessage.warning('没有可用的新头像链接');
    return;
  }
  if (isSubmitting.value) return; // 防止重复点击

  isSubmitting.value = true;
  try {
    // 调用更新用户信息的 PUT 接口
    await axios.put(
      `${baseURL}/user/update`,
      {
        avatarUrl: fileLink.value, // 请求体，只包含新的头像URL
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      }
    );

    ElMessage.success('头像更新成功！');
    emit('update-success', fileLink.value); // 触发成功事件，并传递新URL
    closeDialogAndReset(); // 关闭弹窗并重置状态

  } catch (error) {
    console.error('更新用户信息失败:', error);
    ElMessage.error('头像更新失败，请稍后再试。');
  } finally {
    isSubmitting.value = false;
  }
};

// 点击取消按钮的事件
const onCancel = () => {
    dialog.value = false
  pondRef.value?.removeFiles()
}



</script>

<style scoped>
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

.act-btn {
  height: 50px;
  width: 200px;
  margin: 0 !important;
  padding: 0 !important;
  background-color: transparent;
  border: none;
}
</style>
