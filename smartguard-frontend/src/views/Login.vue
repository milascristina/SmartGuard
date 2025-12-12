
<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

// Variabile reactive (pentru v-model în template)
const username = ref('');
const password = ref('');
const errorMessage = ref('');

const router = useRouter();

const handleLogin = async () => {
  errorMessage.value = ''; // Resetăm eroarea

  try {
    // Asigurati-va ca Spring Boot ruleaza pe 8080 si proxy-ul Vite functioneaza
    const response = await axios.post('/api/auth/login', {
      username: username.value,
      // Numele cheii in JSON trebuie sa fie 'password' pentru a se potrivi cu DTO-ul LoginRequest din Spring
      password: password.value
    });

    // Autentificare reușită (Status 200 OK)
    // ...
    // Autentificare reușită (Status 200 OK)
    if (response.status === 200 && response.data.role) {

      // Stocare date în sesiune locală (pentru navigare ulterioară)
      localStorage.setItem('userRole', response.data.role);
      localStorage.setItem('userId', response.data.id);
      localStorage.setItem('userName', response.data.firstName);
      router.push('/main');
    }
// ...
  } catch (error) {
    // Gestionare erori HTTP (ex: 401 Unauthorized)
    if (error.response && error.response.status === 401) {
      errorMessage.value = 'Nume utilizator sau parolă incorectă.';
    } else {
      console.error('Eroare la logare:', error);
      errorMessage.value = 'Eroare de rețea sau server. Vă rugăm să încercați din nou.';
    }
  }
};
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-container">
      <h2 class="title">Monitorizare Sănătate</h2>
      <p class="subtitle">Autentificare</p>

      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="username">Nume Utilizator</label>
          <input type="text" id="username" v-model="username" required class="input-field" />
        </div>

        <div class="form-group">
          <label for="password">Parolă</label>
          <input type="password" id="password" v-model="password" required class="input-field" />
        </div>

        <button type="submit" class="btn-submit">Logare</button>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

        <p class="register-link">
          Nu ai cont?
          <router-link to="/register">Înregistrează-te acum</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}
.auth-container {
  background-color: var(--color-background-soft);
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5); /* Umbra pentru aspect 3D */
  width: 350px;
}
.title {
  color: var(--color-heading);
  text-align: center;
  margin-bottom: 5px;
}
.subtitle {
  color: var(--color-accent-hover);
  text-align: center;
  margin-bottom: 25px;
  font-size: 1.1em;
}
.form-group {
  margin-bottom: 20px;
}
label {
  display: block;
  margin-bottom: 8px;
  color: var(--color-text);
  font-weight: bold;
}
.input-field {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--color-accent);
  border-radius: 6px;
  background-color: var(--color-background);
  color: var(--color-text);
  box-sizing: border-box;
}
.btn-submit {
  width: 100%;
  padding: 12px;
  background-color: var(--color-accent); /* Mov intens */
  color: white;
  font-size: 1.1em;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.btn-submit:hover {
  background-color: var(--color-accent-hover);
}
.error-message {
  color: #ff8a80; /* Rosu deschis pentru erori */
  margin-top: 15px;
  text-align: center;
}
.register-link {
  margin-top: 20px;
  text-align: center;
  font-size: 0.9em;
}
</style>