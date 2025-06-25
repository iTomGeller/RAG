import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import StoreView from '../views/StoreView.vue'
import LoginView from '../views/LoginView.vue'
import SettingsView from '../views/SettingsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView,
    // },
    {
      path: '/',
      redirect: '/login',//默认重定向
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/chat',
      name: 'chat',
      component: HomeView,
    },
    {
      path: '/store',
      name: 'store',
      component: StoreView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/settings',
      name: 'settings',
      component: SettingsView,
    },
  ],
})



export default router
