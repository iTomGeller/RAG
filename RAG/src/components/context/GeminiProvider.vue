<script setup>
import { provide } from 'vue';
import { useGemini } from '@/service/userGemini';

// 使用 useGemini 获取所有的响应式状态和方法
const geminiContext = useGemini();

// 使用 provide 将这些值提供给所有后代组件
provide('geminiContext', geminiContext);
</script>

<template>
  <slot></slot>
</template>