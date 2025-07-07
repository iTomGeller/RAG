import api from './api'
import { ref } from 'vue'
import { startSSEChat } from '@/service/apiChat'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import router from '@/router'

/**
 * 将 Markdown 文本安全地转换为可用于 v-html 的 HTML 字符串
 */
function safeMarkdownToHtml(text) {
  if (!text || typeof text !== 'string') return ''

  // 使用 marked 解析 Markdown
  const dirtyHtml = marked.parse(text)

  // 使用 DOMPurify 清理 HTML，防止 XSS
  const cleanHtml = DOMPurify.sanitize(dirtyHtml)

  return cleanHtml
}

const input = ref('')
const currentId = ref(null)
const messageContent = ref([])
const sources = ref([])
const loading = ref(false)
const showResult = ref(false) // 控制是否显示结果
let abortChat = null

function init() {
  stopChatSys()
  messageContent.value = []
  console.log('currentId', currentId.value)
  input.value = ''
  sources.value = []
  loading.value = false
  showResult.value = false // 控制是否显示结果
}

init()

async function getPrevPrompt() {
  try {
    const response = await api.get(`/chat/context?memoryId=${currentId.value}`)
    return response.data
  } catch (error) {
    console.error('获取上次对话内容失败:', error)
    throw error
  }
}

function stopChatSys() {
  if (abortChat) {
    abortChat() //中止SSE请求
    loading.value = false // 停止加载状态
    abortChat = null
    console.log('Chat stopped.')
  }
}
function getMessage() {
  if (currentId.value === null) {
    currentId.value = Date.now()
  }
  loading.value = true // 开始加载状态
  let rawMarkdown = '' // 存储原始 Markdown 内容
  let displayIndex = 0 // 当前显示到第几个字符
  const typingSpeed = 20 // 打字速度（毫秒/字符）

  // 添加一个空的 AI 消息用于后续填充（原始文本）
  messageContent.value.push({ type: 'AI', text: '' })

  abortChat = startSSEChat(
    currentId.value,
    input.value,
    (data) => {
      if (data.startsWith('END')) {
        // 收到结束信号，统一转为 HTML
        loading.value = false // 停止加载状态
        console.log('结束')
      } else if (data.startsWith('SOURCES:')) {
        // 提取 SOURCES 部分并解析 JSON
        try {
          const sourceJson = data.replace('SOURCES:', '').trim()
          sources.value = JSON.parse(sourceJson)
          console.log('提取到 SOURCES:', sources.value)
        } catch (e) {
          console.error('解析 SOURCES 失败:', e)
        }
      } else if (data.startsWith('CONTENT:')) {
        showNextChar()
        // 递归函数：逐字显示原始文本
        function showNextChar() {
          if (displayIndex < rawMarkdown.length) {
            displayIndex++
            messageContent.value[messageContent.value.length - 1] = {
              type: 'AI',
              text: rawMarkdown.substring(0, displayIndex),
            }
            setTimeout(showNextChar, typingSpeed)
          } else if (loading.value) {
            setTimeout(showNextChar, typingSpeed)
          } else {
            // loading.value===false，说明getMessage停止了
            // 替换为最终 HTML 展示
            const htmlContent = safeMarkdownToHtml(rawMarkdown)
            messageContent.value[messageContent.value.length - 1] = {
              type: 'AI',
              text: htmlContent,
            }
            console.log(messageContent.value[messageContent.value.length - 1].text)
          }
        }
        console.log('开始')
      } else {
        rawMarkdown += data
        console.log(data)
      }
    },
    (error) => {
      console.error('SSE 错误:', error)
      if (error.message.includes('401')) {
        alert('登录已过期，请重新登录')
        router.push('/login')
      }
    },
  )
}

// function formatTextForVHtml(text) {
//   let html = text.replace(/\n/g, '<br>')
//   html = html.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
//   html = html.replace(/\*(.+?)\*/g, '<em>$1</em>')
//   html = html.replace(/^-\s(.+)$/gm, '• $1')
//   html = html.replace(/```[\w]*\n([\s\S]*?)```/g, '<pre><code>$1</code></pre>')
//   return html
// }

export const ChatService = {
  async deleteChat(id) {
    try {
      if (currentId.value === id) {
        this.changeCurrentChat(Date.now())
      }
      router.push('/home/chat') // 跳转到聊天页面
      const response = await api.delete(`/chat/delete?memoryId=${id}`)
      // 重新获取聊天列表
      await this.getChatList()
      console.log(response.msg)
    } catch (error) {
      console.error('Delete chat error:', error)
      throw error
    }
  },
  async getChatList() {
    try {
      const response = await api.get('/chat/list')
      return response.data
    } catch (error) {
      console.error('Get Chat List error:', error)
      throw error
    }
  },
  addNewChat() {
    showResult.value = false
    init() // 初始化聊天内容
    currentId.value = Date.now() // 使用当前时间戳作为新的聊天 ID
    messageContent.value = [] // 初始化当前聊天的历史内容
    console.log('Starting a new chat' + currentId.value)
  },

  sendMessage() {
    sources.value = [] // 清空之前的 sources
    showResult.value = true
    // 输入内容放入历史中
    messageContent.value.push({ type: 'USER', text: safeMarkdownToHtml(input.value) })
    getMessage()
    input.value = '' // 清空输入框
  },
  stopChat() {
    try {
      stopChatSys()
    } catch (error) {
      console.error('Error stopping chat:', error)
      throw error
    }
  },
  async getPrevContent() {
    messageContent.value = [] // 清空当前消息内容
    sources.value = [] // 清空当前 sources
    const res = await getPrevPrompt() //获取历史对话内容
    res.data.forEach((item) => {
      if (item.type !== 'SYSTEM') {
        messageContent.value.push({
          type: item.type === 'USER' ? 'USER' : 'AI',
          text: safeMarkdownToHtml(item.text),
        })
      }
    })
    console.log('get history')
  },
  async changeCurrentChat(id) {
    stopChatSys() // 停止当前聊天
    currentId.value = id
    this.getPrevContent() // 获取新的聊天内容

    showResult.value = true
    loading.value = false
    input.value = ''
    console.log('showResult:', showResult.value)
    console.log('loading:', loading.value)
    console.log('Switched to chat with ID:', id)
  },

  setInput(text) {
    input.value = text
  },
  getCurrentChatId() {
    return currentId.value
  },
}

export { messageContent, sources, loading, showResult }
