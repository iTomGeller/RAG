<script setup>
import LoginRegister from '@/components/Landing/LoginRegister.vue'
</script>

<template>
  <div class="login-view">
    <div class="login-image">
      <img :src="assets.loginBg" class="login-bg" alt="LoginBg" />
    </div>
    <div class="login-form">
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
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 100vh;
  /* background-color: #f5f5f5; */
  overflow: hidden;
  }

  .login-image{
    /* flex-basis: 60%; */
    witdh:60%;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 2rem;
    /* background-color: #fff; */
  }

  .login-bg{
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .login-view{
    margin: 0;
    border-radius: 0;
    border-radius: 10px;  
    overflow: hidden;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  }

</style>
