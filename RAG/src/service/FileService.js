import api from './api'

const FileService = {
  test() {
    console.log('test')
  },
  async deleteFile(fileId) {
    console.log(`Delete file ${id}:`, fileId)
    try {
      const response = await api.delete('/files', { fileId })
      return response.data
    } catch (error) {
      console.error('Delete file error:', error)
      throw error
    }
  },

  async addFile(baseId, formData) {
    try {
      // 添加请求头，指定 Content-Type 为 multipart/form-data
      const config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      };
      
      const response = await api.post(`/files/upload/${baseId}`, formData, config);
      return response.data
    } catch (error) {
      console.error('Add base error:', error)
      throw error
    }
  },

  async getBaseFiles({ page, pageSize, baseNum }) {
    try {
      const response = await api.get(`/files/${baseNum}`, { params: { page, pageSize } })
      return response.data
    } catch (error) {
      console.error('Get files error:', error)
      throw error
    }
  },
}

export default FileService