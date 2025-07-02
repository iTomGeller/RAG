import api from './api'
import { ref } from 'vue'

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
      console.log('initiateChat')
    } catch (error) {
      console.error('Initiate Chat error:', error)
      throw error
    }
  },
  async getChatPrompt() {
    try {
      return '这里是otterAI，你可以向我提问任何问题，我会尽可能的回答。'
    } catch (error) {
      console.error('Get Chat Prompt error:', error)
      throw error
    }
  },
  async chatTest() {
    try {
      
    } catch (error) {
      console.error('Chat Test error:', error)
      throw error
    }
  },
}

export default ChatService
