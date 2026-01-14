<script setup>
import TheHeader from '../components/TheHeader.vue';
import { useRoute, useRouter } from 'vue-router';
import { computed } from 'vue';

const route = useRoute();
const router = useRouter();

// Date preluate din localStorage după login
const userName = computed(() => localStorage.getItem('userName') || 'Utilizator');
const userRole = computed(() => localStorage.getItem('userRole') || 'PATIENT');
const isDoctor = computed(() => userRole.value === 'DOCTOR');

// Configurația celor 4 module principale
const modules = [
  {
    id: 1,
    title: 'Gestiune Date',
    desc: 'Import CSV Smartwatch și Jurnal manual',
    icon: '📥',
    path: '/data/upload'
  },
  {
    id: 2,
    title: 'Analiză și Grafice',
    desc: 'Vizualizare tendințe ritm cardiac și pași',
    icon: '📊',
    path: '/data/analysis'
  },
  {
    id: 3,
    title: 'Evaluare și Alerte',
    desc: 'Sistem inteligent de praguri critice',
    icon: '🚨',
    path: '/alerts'
  },
  {
    id: 4,
    title: isDoctor.value ? 'Gestiune Pacienți' : 'Medic Curant',
    desc: isDoctor.value ? 'Monitorizare cereri pacienți' : 'Găsește și contactează un doctor',
    icon: '👨‍⚕️',
    path: isDoctor.value ? '/doctor/dashboard' : '/find-doctor'
  }
];

const pageTitle = computed(() => {
  switch (route.path) {
    case '/data/upload': return 'Modul 1: Încărcare Date';
    case '/data/analysis': return 'Modul 2: Vizualizare și Analiză';
    case '/alerts': return 'Modul 3: Evaluare și Alerte AI';
    case '/find-doctor': return 'Modul 4: Găsește un Doctor';
    case '/doctor/dashboard': return 'Modul 4: Panou Control Medic';
    default: return 'SmartGuard Dashboard';
  }
});
</script>

<template>
  <div class="main-layout">
    <TheHeader />

    <main class="main-content-area">
      <header class="page-header">
        <h1>{{ pageTitle }}</h1>
        <p v-if="route.path === '/main'" class="welcome-text">
          Bine ai venit, <strong>{{ userName }}</strong>! Gestionează-ți sănătatea de aici.
        </p>
      </header>

      <div v-if="route.path === '/main'" class="dashboard-grid">
        <div
            v-for="mod in modules"
            :key="mod.id"
            :class="['menu-card', 'card-' + mod.id]"
            @click="router.push(mod.path)"
        >
          <div class="card-icon">{{ mod.icon }}</div>
          <div class="card-info">
            <h3>{{ mod.title }}</h3>
            <p>{{ mod.desc }}</p>
          </div>
          <div class="card-arrow">→</div>
        </div>
      </div>

      <div v-else class="module-container">
        <div class="placeholder-content">
          <div class="info-box">
            <p>Ești în secțiunea: <strong>{{ pageTitle }}</strong></p>
            <p>Aici va fi implementată logica specifică pentru acest modul.</p>
            <button @click="router.push('/main')" class="btn-back">Înapoi la Meniu</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.main-layout {
  min-height: 100vh;
  background-color: var(--color-background);
}

.main-content-area {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  margin-bottom: 40px;
  border-bottom: 2px solid var(--color-background-soft);
  padding-bottom: 20px;
}

.welcome-text {
  font-size: 1.1em;
  color: var(--color-text);
  margin-top: 10px;
}

/* Grid-ul pentru module */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 25px;
}

.menu-card {
  background-color: var(--color-background-soft);
  border-radius: 16px;
  padding: 30px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.menu-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.2);
  background-color: var(--color-background-mute);
}

/* Culori specifice pentru marginea fiecărui card */
.card-1 { border-left: 6px solid #42b983; } /* Verde */
.card-2 { border-left: 6px solid #3498db; } /* Albastru */
.card-3 { border-left: 6px solid #e74c3c; } /* Roșu */
.card-4 { border-left: 6px solid #9b59b6; } /* Mov */

.card-icon {
  font-size: 3em;
}

.card-info h3 {
  margin: 0 0 8px 0;
  color: var(--color-heading);
  font-size: 1.4em;
}

.card-info p {
  margin: 0;
  color: var(--color-text);
  font-size: 0.95em;
  line-height: 1.4;
}

.card-arrow {
  margin-left: auto;
  font-size: 1.5em;
  opacity: 0.5;
}

/* Stiluri sub-pagini */
.module-container {
  background: var(--color-background-soft);
  border-radius: 12px;
  padding: 60px 40px;
  text-align: center;
}

.btn-back {
  margin-top: 25px;
  padding: 12px 25px;
  background-color: var(--color-accent);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.btn-back:hover {
  background-color: var(--color-accent-hover);
}
</style>