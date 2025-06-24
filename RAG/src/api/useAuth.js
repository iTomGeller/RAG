import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { auth } from './auth'

export function useAuth() {
  const router = useRouter()
  const loading = ref(false)
  const errorMessage = ref('')
  const isCodesent = ref(false)
  const codeCountdown = ref(0)
  let countdownTimer = null

  //邮箱格式验证
  const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    return emailRegex.test(email) //判断是否符合上述正则表达式
  }

  //登陆方法
  const login = async (credentials) => {
    try {
        loading.value = true
        errorMessage.value = ''
        const response = await auth.login(credentials)

        //登陆成功，将用户信息存储到本地
        localStorage.setItem('userInfos', JSON.stringify(response.userVO)) //user value objedt
        localStorage.setItem('token', response.token)
        router.push('/home') // 跳转到首页
    } catch (error) {
        throw error
    } finally {
        loading.value = false
    }
  }

  //注册方法
  const register = async (userData) => {
    if(!validateEmail(userData.email)) {
      errorMessage.value = '请输入有效的邮箱地址'
      return Promise.reject(new Error('Invalid email'))
    }

    if( userData.password !== userData.confirmPassword) {
      errorMessage.value = '两次输入的密码不一致'
      return Promise.reject(new Error('Passwords do not match'))
    }   

    try {
        loading.value = true
        errorMessage.value = ''

        await auth.register({
            username: userData.username,
            email: userData.email,
            password: userData.password,
            captcha: userData.verificationCode
        })


        // 注册成功后，跳转到登录页面
        await login({
            email: userData.email,
            password: userData.password
        })
    }catch (error) {
        throw error
    } finally {
        loading.value = false
    }
  }


  // 发送验证码
    const sendVerificationCode = async (email) => {
        if(!validateEmail(email.email)) {
            errorMessage.value = ' 请输入正确的邮箱格式'
            return Promise.reject(new Error('Invalid email'))
        }

        if(isCodesent.value) {
            return Promise.reject(new Error('验证码已发送，请稍后再试'))
        }

        try {
            loading.value = true
            errorMessage.value = ''

            await auth.sendVerificationCode(email.email)

            isCodesent.value = true
            codeCountdown.value = 60

            countdownTimer = setInterval(() => {
                if (codeCountdown.value > 0) {
                    codeCountdown.value--
                } else {    
                    clearInterval(countdownTimer)
                    isCodesent.value = false
                }
            }, 1000)
        } catch (error) {
            throw error
        } finally {
            loading.value = false
        }
    }

  return {
    loading,
    errorMessage,
    isCodesent,
    codeCountdown,
    login,
    register,
    sendVerificationCode,
    validateEmail
  }
}
