import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import StoreView from '../views/StoreView.vue'
import LoginView from '../views/LoginView.vue'
import SettingsView from '../views/SettingsView.vue'
import MainView from '../views/MainView.vue'

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
      component: LoginView,
    },
    {
      path: '/home',
      component: MainView,
      meta: { requiresAuth: false },
      children: [
        {
          path: 'chat',
          component: HomeView,
        },
        {
          path: 'store',
          component: StoreView,
        },
        {
          path: 'login',
          component: LoginView,
        },
        {
          path: 'settings',
          component: SettingsView,
        },
      ],
    },
    {
      path: '/chat',
      component: HomeView,
    },
    {
      path: '/store',
      component: StoreView,
    },
    {
      path: '/login',
      component: LoginView,
    },
    {
      path: '/settings',
      component: SettingsView,
    },
  ],
})

// 登录拦截
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('userInfo')
  const token = localStorage.getItem('token')
  console.log(isLoggedIn)
  console.log(token)
  
  const publicPages = ['/login']
  const isPublic = publicPages.includes(to.path)

  if(!isLoggedIn && !isPublic)
    {
      console.log("拦截！！")
      return next('/login')
    } 

  if(isLoggedIn && isPublic) return next('/')
    next()

})

export default router
