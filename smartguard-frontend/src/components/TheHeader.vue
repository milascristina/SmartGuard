<script setup>
import {useRouter} from 'vue-router';
import {computed} from 'vue';

const router = useRouter();

// Functie pentru a gestiona delogarea
const handleLogout = () => {
  // Ștergem toate datele din localStorage legate de sesiune
  localStorage.removeItem('userRole');
  localStorage.removeItem('userId');
  localStorage.removeItem('userName');

  // Redirecționăm utilizatorul către pagina de login
  router.push('/');
};

// Verificăm dacă utilizatorul este doctor
const isDoctor = computed(() => {
  return localStorage.getItem('userRole') === 'DOCTOR';
});
</script>

<template>
  <header class="navbar">
    <div class="nav-left">
      <router-link to="/profile" class="nav-item profile-icon" title="Profilul meu">
        <i class="fas fa-user-circle"></i></router-link>
    </div>

    <nav class="nav-links">
      <router-link to="/data/upload" class="nav-item">
        Încărcare Date
      </router-link>
      <router-link to="/data/analysis" class="nav-item">
        Vizualizare & Analiză
      </router-link>
      <router-link to="/alerts" class="nav-item">
        Evaluare & Alerte
      </router-link>

      <router-link v-if="!isDoctor" to="/find-doctor" class="nav-item">
        Găsește un Doctor
      </router-link>
    </nav>

    <div class="nav-right">
      <button @click="handleLogout" class="nav-item btn-logout">
        Logout
      </button>
    </div>
  </header>
</template>

<style scoped>
/* Culorile sunt preluate din variabilele presupuse: var(--color-accent) și var(--color-background-soft) */
.navbar {
  background-color: var(--color-accent, #6200EE); /* Fundal Mov */
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.nav-links {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: white;
  text-decoration: none;
  padding: 8px 15px;
  border-radius: 5px;
  transition: background-color 0.3s, color 0.3s;
  font-weight: 500;
  display: flex;
  align-items: center;
}

/* Stil pentru hover/focus */
.nav-item:hover,
.nav-item:focus {
  background-color: var(--color-accent-hover, #3700B3); /* O nuanță mai închisă de mov */
}

/* Iconiță de Profil */
.profile-icon i {
  font-size: 1.8em;
}

/* Buton Logout */
.btn-logout {
  background: none;
  border: 1px solid white;
  cursor: pointer;
  margin-left: 20px;
}

.btn-logout:hover {
  background-color: white;
  color: var(--color-accent, #6200EE); /* Textul devine mov la hover */
}
</style>