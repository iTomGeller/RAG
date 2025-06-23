<script setup>
import Sidebar from '../components/Siderbar/Sidebar.vue'
import Main from '../components/Main/Main.vue'
</script>

<template>
  <div class="home">
    <Sidebar />
    <Main />
  </div>
</template>

<style scoped>
.home {
  display: flex; /* Makes the children (Sidebar and Main) arrange in a row */
  height: 100vh; /* Ensures the container takes at least the full viewport height */
  width: 100%; /* Ensures the container takes the full width */
}

/* Optional: You might want to adjust the width of Sidebar and Main */
/* If your Sidebar has a fixed width, Main can take the rest */
/*
.sidebar {
  flex-shrink: 0; // Prevent sidebar from shrinking
  width: 250px; // Example fixed width
}

.main {
  flex-grow: 1; // Allow main to take up remaining space
  overflow-y: auto; // Add scroll to main if content overflows
}
*/
</style>