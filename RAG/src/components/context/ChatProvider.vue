<script setup>
import ChatService from '@/service/ChatService'
import { ref, provide } from 'vue'

const currentChatId = ref(Date.now())
const showResult = ref(false)

provide('currentChatId', currentChatId)
provide('showResult', showResult)
</script>

<template>
  <slot></slot>
</template>
