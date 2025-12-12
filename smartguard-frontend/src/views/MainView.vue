<script setup>
import TheHeader from '../components/TheHeader.vue';
import { useRoute, useRouter } from 'vue-router';
import { computed } from 'vue';

const route = useRoute();
const router = useRouter();

const userName = computed(() => {
  // Returnează numele stocat sau 'Utilizator' ca fallback
  return localStorage.getItem('userName') || 'Utilizator';
});

// Verificăm dacă utilizatorul este doctor (pentru a afișa/ascunde anumite elemente)
const isDoctor = computed(() => {
  return localStorage.getItem('userRole') === 'DOCTOR';
});

// Funcție pentru a genera titlul paginii în funcție de ruta curentă
const pageTitle = (path) => {
  switch (path) {
    case '/data/upload': return 'Încărcare Date';
    case '/data/analysis': return 'Vizualizare și Analiză';
    case '/alerts': return 'Evaluare și Alerte';
    case '/find-doctor': return 'Găsește un Doctor';
    case '/profile': return 'Profilul Utilizatorului';
    default: return 'Pagina Principală SmartGuard';
  }
};

// Funcție pentru navigare rapidă de la butonul CTA
const goToFindDoctor = () => {
  router.push('/find-doctor');
};
</script>

<template>
  <div>
    <TheHeader />

    <div class="main-content-area">
      <h1>{{ pageTitle(route.path) }}</h1>

      <div v-if="route.path === '/main'" class="dashboard-area">

        <div v-if="!isDoctor" class="cta-section">
          <p>Ai nevoie de o consultație medicală?</p>
          <button @click="goToFindDoctor" class="btn-cta">
            Găsește un Doctor Acum
          </button>
        </div>
      </div>

      <p v-else>
        Conținutul funcționalității **{{ pageTitle(route.path) }}** va fi implementat aici.
      </p>

    </div>
  </div>
</template>

<style scoped>
.main-content-area {
  padding: 30px;
  color: var(--color-text);
}
.main-content-area h1 {
  color: var(--color-heading);
  margin-bottom: 20px;
}

/* Stiluri pentru dashboard-ul central */
.dashboard-area {
  text-align: center;
  padding: 50px 0;
}
.welcome-message {
  font-size: 1.2em;
  margin-bottom: 40px;
}

/* Stiluri pentru butonul mare Find Doctor (CTA) */
.cta-section {
  margin-top: 50px;
}
.cta-section p {
  font-size: 1.1em;
  color: var(--color-accent);
  margin-bottom: 15px;
}
.btn-cta {
  padding: 15px 30px;
  background-color: var(--color-accent); /* Mov */
  color: white;
  font-size: 1.3em;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s, transform 0.2s;
}
.btn-cta:hover {
  background-color: var(--color-accent-hover);
  transform: translateY(-2px);
}
</style>