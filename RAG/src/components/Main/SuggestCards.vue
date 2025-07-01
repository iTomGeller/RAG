<template>
  <div class="cards">
    <div
      class="card"
      v-for="(item, index) in selectedSuggestions"
      :key="index"
      @click="handleCardClick(t(`suggestions.S${item.toString().padStart(3, '0')}`))"
    >
      <p>{{ t(`suggestions.S${item.toString().padStart(3, '0')}`) }}</p>
      <!-- <img :src="assets.compass_icon" alt="指南针图标" /> -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { inject } from 'vue';
import { useI18n } from 'vue-i18n';
import { assets } from '@/assets/assets';

const { t } = useI18n();
const { onSent } = inject('geminiContext');

const selectedSuggestions = ref([]);

const handleCardClick = (prompt) => {
  onSent(prompt);
};

// 从 1 到 20 中随机抽取 4 个不重复的编号
function getRandomSuggestions() {
  const arr = Array.from({ length: 20 }, (_, i) => i + 1);
  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]];
  }
  return arr.slice(0, 4);
}

onMounted(() => {
  selectedSuggestions.value = getRandomSuggestions();
});
</script>

<style scoped>
@import './Main.css';
</style>
