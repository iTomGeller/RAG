import { axiosClient } from "./axios-api";

export const auth = {
    // 用户登录
    login(data) {
        return axiosClient.post('/login',data)
    },

    // 用户注册
    register(data) {
        return axiosClient.post('/register', data)
    },

    //发送验证码
    sendVerificationCode(email) {
        const encodedEmail = encodeURIComponent(email);
        return axiosClient.post(`/send-code?email=${encodedEmail}`);
    },

}