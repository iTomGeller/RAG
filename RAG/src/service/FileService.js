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
}

export default FileService
