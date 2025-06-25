<template>
  <div class="login-register">
    <div class="form-container">
      <h2 class="title">{{ isLogin ? '登陆' : '注册' }}</h2>
      <div class="error" v-if="errorMessage">{{ errorMessage }}</div>
      <form @submit.prevent="handleSubmit">
        <template v-if="!isLogin">
          <div class="input-group">
            <label for="username">用户名</label>
            <input type="text" id="username" v-model="username" required />
          </div>
        </template>

        <div class="input-group">
          <label for="email">邮箱</label>
          <input type="email" id="email" v-model="email" required />
        </div>

        <template v-if="!isLogin && isEmailValid">
          <div calss="input-group">
            <label for="verificationCode">验证码</label>
            <div class="code-input">
              <input text="text" id="verificationCode" v-model="verificationCode" required />
              <button type="button" @click="handleSendCode" :disabled="isCodeSent">
                {{ isCodeSent ? `重新获取(${codeCountdown})` : '发送验证码' }}
              </button>
            </div>
          </div>
        </template>


        <div class="input-group">
          <label for="password">密码</label>
          <input class="ink-input" type="password" id="password" v-model="password" required />
        </div>
        <div class="input-group" v-if="!isLogin">
          <label for="confirmPassword">确认密码</label>
          <input type="password" id="confirmPassword" v-model="confirmPassword" required />
        </div>
        <button class="ink-button" type="submit" :disabled="loading" @click="handleSubmit">
          {{ loading ? '处理中...' : isLogin ? '开始使用' : '启名' }}
        </button>   
      </form>
      <p class="toggle-form" @click="toggleForm">
        {{ isLogin ? '没有账号？注册' : '已有账号？登录' }}
      </p>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { useAuth } from '@/api/useAuth'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const isLogin = ref(true)
    const username = ref('')
    const email = ref('')
    const password = ref('')
    const confirmPassword = ref('')
    const verificationCode = ref('')
    const isCodeSent = ref(false)
    const codeCountdown = ref(0)
    const router = useRouter()
    let countdownTimer = null

    const { loading, errorMessage, login, register, sendVerificationCode } = useAuth()

    //判断邮箱格式是否正确
    const isEmailValid = computed(() => {
      const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
      return emailPattern.test(email.value)
    })

    const toggleForm = () => {
      isLogin.value = !isLogin.value
      // Reset field
      username.value = ''
      email.value = ''
      password.value = ''
      confirmPassword.value = ''
      verificationCode.value = ''
      isCodeSent.value = false
      codeCountdown.value = 0
      if (countdownTimer) {
        clearInterval(countdownTimer)
        countdownTimer = null
      }
    }

    const handleSubmit = async () => {
      try {
        if (isLogin.value) {
          await login({
            email: email.value,
            password: password.value,
          })

          //登陆成功，重定向到主页
          console.log('登陆成功')
          router.push('/home')
        } else {
          console.log('注册')
          console.log(username.value)
          console.log(email.value)
          console.log(password.value)
          console.log(confirmPassword.value)
          console.log(verificationCode.value)


          await register({
            username: username.value,
            email: email.value,
            password: password.value,
            confirmPassword: confirmPassword.value,
            verificationCode: verificationCode.value
          })

          //注册成功，重定向到登陆页
          router.push('/login')
        }
      } catch (error) {
        //拦截器错误处理
        if (error.message) {
          errorMessage.value = error.message
        } else {
          //处理其他错误
          errorMessage.value = '发生错误，请稍后再试'
          console.error('发生错误:', error)
        }
      }
    }

    const handleSendCode = async () => {
      if (!isEmailValid.value) {
        errorMessage.value = '请输入有效的邮箱地址'
        return
      }
      try {
        await sendVerificationCode({ email: email.value })
        isCodeSent.value = true
        codeCountdown.value = 60
        errorMessage.value = ''

        countdownTimer = setInterval(() => {
          if (codeCountdown.value > 0) {
            codeCountdown.value--
          } else {
            clearInterval(countdownTimer)
            isCodeSent.value = false
          }
        }, 1000)
      } catch (error) {
        console.error('发送验证码失败', error)
        errorMessage.value = '发送验证码失败，稍请后再试'
      }
    }

    return {
      isLogin,
      username,
      email,
      password,
      confirmPassword,
      verificationCode,
      isCodeSent,
      codeCountdown,
      loading,
      errorMessage,
      isEmailValid,
      toggleForm,
      handleSubmit,
      handleSendCode,
    }
  }
}
</script>

<style scoped>
.login-register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.form-container {
  padding: 40px 50px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  width: 400px;
  text-align: center;
}

.form-container h2 {
  margin-bottom: 30px;
  font-weight: normal;
  font-size: 28px;
  font-family: 'Ma Shan Zheng', cursive;
}

.iput-group {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  text-align: left;
}

.input-group label {
  flex: 0 0 80px;
  font-size: 16px;
}

.input-group input {
  flex: 1;
  padding: 12px 15px;
  font-size: 16px;
}

button {
  width: 100%;
  padding: 12px;
  font-size: 18px;
  margin-top: 10px;
}

.toggle-form {
  margin-top: 24px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
  text-decoration: underline;
  text-underline-offset: 4px;
  padding: 4px 8px;
}

.toggle-form:hover {
  text-decoration-thickness: 2px;
}

.error {
  color: #8b0000;
  font-size: 16px;
  margin: 10px 0;
  text-align: center;
  font-family: 'SimSun', serif;
}

.code-input {
  display: flex;
  gap: 10px;
}
</style>
