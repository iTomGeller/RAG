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
      @addfile="handleAddFile"
      @removefile="handleRemoveFile"
    />

    <!-- 已添加文件列表及删除按钮 -->
    <div class="file-list">
      <div v-for="(file, index) in files" :key="index" class="file-item">
        <span>{{ file.filename }}</span>
        <button class="remove-btn" @click="removeFile(index)">×</button>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="actions">
      <el-button @click="uploadFiles">确认上传</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import vueFilePond from 'vue-filepond';
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size';
import FileService from '@/service/FileService';

// 注册插件并创建 FilePond 组件
const FilePond = vueFilePond(FilePondPluginFileValidateSize);

// 定义 props 接收 baseId
const props = defineProps({
  baseId: {
    type: Number,
    required: true
  }
});

// 当前选择的文件列表
const files = ref([]);
const pond = ref(null);

// 添加文件到本地列表
const handleAddFile = (error, file) => {
  if (!error) {
    files.value.push(file);
  }
};

// 移除文件
const handleRemoveFile = (file) => {
  const index = files.value.findIndex(f => f.id === file.id);
  if (index > -1) {
    files.value.splice(index, 1);
  }
};

// 自定义移除文件方法
const removeFile = (index) => {
  const file = files.value[index];
  pond.value.removeFile(file);
};

// 确认上传所有文件
const uploadFiles = async () => {
  for (const file of files.value) {
    try {
      // 创建 FormData 对象
      const formData = new FormData();
      // 将文件添加到 FormData 中，参数名为 'file'
      formData.append('file', file.file);
      // 调用 FileService.addFile 方法
      await FileService.addFile(props.baseId, formData);
      console.log(`文件 ${file.filename} 上传成功`);
    } catch (err) {
      console.error(`文件 ${file.filename} 上传失败`, err);
    }
  }
  // 清空文件列表
  files.value = [];
  pond.value.removeFiles();
};
</script>

<style scoped>
.file-upload-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}
.file-list {
  margin-top: 20px;
}
.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #eee;
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