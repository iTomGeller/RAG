import api from './api'

const BaseService = {
  async getUserBaseInfo({ page, pageSize }) {
    try {
      const response = await api.get('/knowledgebase', { params: { page, pageSize } })
      return response.data
    } catch (error) {
      console.error('Get user base info error:', error)
      throw error
    }
  },
  async addBase({ name, type }) {
    try {
      const response = await api.post('/knowledgebase', { name, type })
      return response.data
    } catch (error) {
      console.error('Add base error:', error)
      throw error
    }
  },
}

export default BaseService
