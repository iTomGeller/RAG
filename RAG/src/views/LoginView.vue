<script setup>
import LoginRegister from '@/components/Landing/LoginRegister.vue'
</script>

<template>
  <div class="login-view">
    <div class="login-left">
      <img :src="assets.loginBg" class="login-bg" alt="LoginBg" />
    </div>
    <div class="login-right">
      <LoginRegister />
    </div>
  </div>
</template>

<script>
import { assets } from '@/assets/assets';
</script>

<style scoped>
.login-view {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  /* background: #0d1117; */
}

.login-left {
  flex: 1.5;
  /* background-color: #000; */
}

.login-bg {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.9);
}

.login-right {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}


</style>
