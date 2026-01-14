import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import MainView from '../views/MainView.vue'
import InputModule from '../views/InputModule.vue'
import AnalysisModule from '../views/AnalysisModule.vue'
import AlertsModule from '../views/AlertsModule.vue'
// 1. ADAUGĂ IMPORTUL PENTRU MODULUL 4
import DoctorModule from '../views/DoctorModule.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login,
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
    },
    {
      path: '/main',
      name: 'main',
      component: MainView,
      meta: { requiresAuth: true }
    },
    // Modulul 1: Gestiune Date
    {
      path: '/data/upload',
      name: 'upload',
      component: InputModule,
      meta: { requiresAuth: true }
    },
    // Modulul 2: Analiză și Grafice
    {
      path: '/data/analysis',
      name: 'analysis',
      component: AnalysisModule,
      meta: { requiresAuth: true }
    },
    // Modulul 3: Evaluare și Alerte AI
    {
      path: '/alerts',
      name: 'alerts',
      component: AlertsModule,
      meta: { requiresAuth: true }
    },
    // Modulul 4: Medic Curant (ACTUALIZAT)
    {
      path: '/find-doctor',
      name: 'find-doctor',
      component: DoctorModule, // Schimbat din MainView în DoctorModule
      meta: { requiresAuth: true }
    },
  ],
})

// Logica de protecție a rutelor (Navigation Guard)
router.beforeEach((to, from, next) => {
  const userId = localStorage.getItem('userId');

  if (to.meta.requiresAuth && !userId) {
    // Dacă pagina cere autentificare și user-ul nu e logat, redirect la Login
    next('/');
  } else if ((to.name === 'login' || to.name === 'register') && userId) {
    // Dacă user-ul este deja logat, nu îl lăsăm să vadă iar pagina de Login
    next('/main');
  } else {
    next();
  }
});

export default router