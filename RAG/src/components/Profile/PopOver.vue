<template>
    <div class="profile-popover">
        <div class="profile-header">
            <img class="avatar" :src="avatarUrl" alt="头像" />
            <div class="info">
                <div class="nickname">{{ nickname }}</div>
                <div class="account">{{ account }}</div>
            </div>
        </div>
        <div class="options">
            <button @click="onOption1">选项一</button>
            <button @click="onOption2">选项二</button>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const avatarUrl = ref('https://via.placeholder.com/48')
const nickname = ref('昵称示例')
const account = ref('账号: example123')

function onOption1() {
    // 处理选项一
    alert('选项一被点击')
}

function onOption2() {
    // 处理选项二
    alert('选项二被点击')
}
</script>

<style scoped>
.profile-popover {
    width: 260px;
    padding: 20px;
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.08);
}
.profile-header {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
}
.avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    margin-right: 14px;
}
.info {
    flex: 1;
}
.nickname {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 4px;
}
.account {
    font-size: 14px;
    color: #888;
}
.options {
    display: flex;
    justify-content: space-between;
}
.options button {
    flex: 1;
    margin: 0 4px;
    padding: 8px 0;
    border: none;
    border-radius: 6px;
    background: #f5f5f5;
    cursor: pointer;
    font-size: 15px;
    transition: background 0.2s;
}
.options button:hover {
    background: #e0e0e0;
}
</style>