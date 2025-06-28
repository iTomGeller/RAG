<template>
  <el-select class="language-select" v-model="locale" placeholder =" language" >
    <el-option v-for ="item in languages" :key="item.value" :label="item.label" :value="item.value"></el-option>
  </el-select>
</template>

<script setup>
import { useI18n } from 'vue-i18n';
import { watch } from 'vue';

const {locale} = useI18n();

const languages= [
    {value: 'en', label: 'English'},
    {value: 'zh', label: '中文'}
]

//监听locale变化，存储到localStorage持久化
watch(locale,(newlocale)=>{
    console.log('language changed to',newlocale);
    localStorage.setItem('language',newlocale);
})
</script>

<style scoped> 
.language-select {
  width:100px;
}

</style>