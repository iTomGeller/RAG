<template>
    <div class="cards">
        <div class="card" @click="handleCardClick('建议即将到来的公路旅行中值得看的美丽景点')">
            <p>建议即将到来的公路旅行中值得看的美丽景点</p>
            <img :src="assets.compass_icon" alt="指南针图标" />
        </div>
        <div class="card" @click="handleCardClick('简要总结一段文本的要点：城市规划')">
            <p>简要总结一段文本的要点：城市规划</p>
            <img :src="assets.bulb_icon" alt="灯泡图标" />
        </div>
        <div class="card" @click="handleCardClick('为我们的工作静修会集思广益团队建设活动')">
            <p>为我们的工作静修会集思广益团队建设活动</p>
            <img :src="assets.message_icon" alt="消息图标" />
        </div>
        <div class="card" @click="handleCardClick('提高以下代码的可读性')">
            <p>提高以下代码的可读性</p>
            <img :src="assets.code_icon" alt="代码图标" />
        </div>
    </div>

</template>

<script setup>
import { assets } from '@/assets/assets'; // Assuming assets path remains the same
import { inject } from 'vue';
const {
    onSent,
} = inject('geminiContext');

const handleCardClick = (prompt) => {
    onSent(prompt);
};

</script>

<style scoped>
@import './Main.css';
</style>