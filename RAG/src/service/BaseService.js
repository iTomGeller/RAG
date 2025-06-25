import api from './api'

const BaseService = {
  async getUserBaseInfo({ page, pageSize }) {
    try {
      //   page = String(page)
      //   pageSize = String(pageSize)
      //   console.log('Get user base info params:', { page, pageSize })
      const response = await api.get('/knowledgebase', { params: { page, pageSize } })
      console.log('Get user base info:', response.data)
      return response.data
    } catch (error) {
      console.error('Get user base info error:', error)
      throw error
    }
  },

  async getBaseFiles({ page, pageSize, baseNum }) {
    try {
      const response = await api.get(`/files/${baseNum}`,{params: { page, pageSize }})
      console.log('Get files:', response.data)
      return response.data
    } catch (error) {
      console.error('Get files error:', error)
      throw error
    }
  },

  async addBase({ name, type }) {
    try {
      const response = await api.post('/knowledgebase', { name, type })
      console.log('Add base:', response.data)
      return response.data
    } catch (error) {
      console.error('Add base error:', error)
      throw error
    }
  },
}

export default BaseService
