import { axiosClient } from "./axios-api";

const getUserInfo = () => {
    //获取本地信息
    const info = localStorage.getItem('userInfos');
    var userInfo = null;
    if (info) {
        userInfo = JSON.parse(info);
    }else {
        console.error('No user info found in local storage');
    }
    return userInfo;
}

// 用户信息更新
 

export { getUserInfo };