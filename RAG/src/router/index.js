import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import StoreView from '../views/StoreView.vue'
import LoginView from '../views/LoginView.vue'
import SettingsView from '../views/SettingsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
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
      path:'/login',
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


//登录拦截
// router.beforeEach((to, from, next) => {
//   const isLoggedIn = localStorage.getItem('userInfo')
//   const publicPages = ['/login']
//   const isPublic = publicPages.includes(to.path)
  
//   if(!isLoggedIn && !isPublic) return next('/login')
//   if(isLoggedIn && isPublic) return next('/')
//     next()


// })

export default router
