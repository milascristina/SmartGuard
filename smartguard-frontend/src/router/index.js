import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import MainView from '../views/MainView.vue' // NOU!

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      // RUTA PRINCIPALA: Login Page (neautentificat)
      path: '/',
      name: 'login',
      component: Login,
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
    },
    // RUTELE CU ACCES RESTRICȚIONAT (Pagina Principală cu Meniu)
    {
      // Ruta de baza dupa login
      path: '/main',
      name: 'main',
      component: MainView,
      meta: { requiresAuth: true }
    },
    // Rutele funcționale (care vor folosi Header-ul si content-ul din MainView)
    { path: '/data/upload', name: 'upload', component: MainView, meta: { requiresAuth: true } },
    { path: '/data/analysis', name: 'analysis', component: MainView, meta: { requiresAuth: true } },
    { path: '/alerts', name: 'alerts', component: MainView, meta: { requiresAuth: true } },
    { path: '/find-doctor', name: 'find-doctor', component: MainView, meta: { requiresAuth: true } },
  ],
})

// Adaugam logica de protectie a rutelor (Guard)
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('userId');

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Daca ruta cere autentificare, dar utilizatorul nu este logat, il trimitem la login
    next('/');
  } else if ((to.name === 'login' || to.name === 'register') && isAuthenticated) {
    // Daca utilizatorul este deja logat si incearca sa acceseze login/register
    next('/main'); // Il trimitem la pagina principala
  } else {
    next();
  }
});

export default router