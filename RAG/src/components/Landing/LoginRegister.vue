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
  width: 100%;
  max-width: 420px;
  padding: 48px;
  border-radius: 20px;
  backdrop-filter: blur(14px);
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow:
    0 8px 30px rgba(0, 0, 0, 0.2),
    inset 0 0 0 0.5px rgba(255, 255, 255, 0.08);
  color: #f0f0f0;
  font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
  transition: all 0.4s ease;
}

.form-container h2 {
  margin-bottom: 24px;
  font-size: 30px;
  font-weight: 500;
  letter-spacing: 1px;
  color: #101b30ee;
  text-align: center;
}

.input-group {
  margin-bottom: 22px;
  text-align: left;
}

.input-group label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  color: #ccc;
}

.input-group input {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  font-size: 15px;
  color: #e0e0e0;
  transition: all 0.25s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.25);
}

.input-group input:focus {
  outline: none;
  background-color: rgba(255, 255, 255, 0.09);
  border: 1px solid #409eff;
  box-shadow: 0 0 6px #409eff88;
}

button {
  width: 100%;
  padding: 13px;
  font-size: 17px;
  font-weight: 500;
  border: none;
  border-radius: 10px;
  /* background: linear-gradient(135deg, #409eff, #6c9cff); */
  /* color: white; */
  cursor: pointer;
  box-shadow: 0 0 12px rgba(120, 128, 135, 0.3);
  transition: all 0.3s ease;
}

button:hover:not(:disabled) {
  box-shadow: 0 0 18px rgba(191, 210, 230, 0.5);
  background: linear-gradient(135deg, #ccd3d9, #bed1f6);
}

button:disabled {
  background: #2c3e50;
  color: #aaa;
  cursor: not-allowed;
  box-shadow: none;
}

.code-input {
  display: flex;
  gap: 10px;
}

.code-input input {
  flex: 1;
}

.code-input button {
  flex-shrink: 0;
  padding: 0 16px;
}

.toggle-form {
  margin-top: 20px;
  font-size: 15px;
  color: #bbbbbb;
  text-align: center;
  cursor: pointer;
}

.toggle-form:hover {
  color: hsl(210, 48%, 22%);
  text-decoration: underline;
}

.error {
  color: #ff4d4f;
  margin-bottom: 16px;
  text-align: center;
  font-size: 14px;
}


</style>
