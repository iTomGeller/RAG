import api from './api'

const FeedbackService = {

  async addFeedback(content) {
    try {
      const response = await api.post('/feedback', {content})
      return response.data
    } catch (error) {
      console.error('Add feedback error:', error)
      throw error
    }
  },

  async getRecentFeedbacks({ page, pageSize}) {
    try {
      const response = await api.get(`/feedback`, { params: { page, pageSize } })
      return response.data
    } catch (error) {
      console.error('Get old feedbacks error:', error)
      throw error
    }
  },

  async deleteFeedback(id) {
    console.log('deleteFeedback', id)
    try {
      const response = await api.delete(`/feedback`,{params: {id}})
      return response.data
      } catch (error) {
        console.error('Delete feedback error:', error)
        throw error
      }
  }
}

export default FeedbackService