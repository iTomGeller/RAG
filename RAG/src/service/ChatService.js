import api from './api'

const ChatService = {
  async getChatList() {
    try {
      const response = await api.get('/chat/list')
      return response.data
    } catch (error) {
      console.error('Get Chat List error:', error)
      throw error
    }
  },
  async initiateChat({ memoryId, message }) {
    try {
      const response = await api.post('/chat', { memoryId, message })
      return response.data
    } catch (error) {
      console.error('Get Chat History error:', error)
      throw error
    }
  },
}

export default ChatService
