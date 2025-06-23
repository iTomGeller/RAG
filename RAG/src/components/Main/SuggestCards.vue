<template>
    <div class="cards">
        <div class="card" @click="handleCardClick('Suggest beautiful places to see on an upcoming road trip')">
            <p>Suggest beautiful places to see on an upcoming road trip</p>
            <img :src="assets.compass_icon" alt="Compass Icon" />
        </div>
        <div class="card" @click="handleCardClick('Briefly summarize the main points of a text: Urban planning')">
            <p>Briefly summarize the main points of a text: Urban planning</p>
            <img :src="assets.bulb_icon" alt="Bulb Icon" />
        </div>
        <div class="card" @click="handleCardClick('Brainstorm team bonding activities for our work retreat')">
            <p>Brainstorm team bonding activities for our work retreat</p>
            <img :src="assets.message_icon" alt="Message Icon" />
        </div>
        <div class="card" @click="handleCardClick('Improve the readability of the following code')">
            <p>Improve the readability of the following code</p>
            <img :src="assets.code_icon" alt="Code Icon" />
        </div>
    </div>

</template>

<script setup>
import { assets } from '@/assets/assets'; // Assuming assets path remains the same

const setInput = (value) => {
  input.value = value;
};
const handleCardClick = (prompt) => {
  setInput(prompt);
  onSent(prompt);
};

</script>

<style scoped>
@import './Main.css'; 
</style>