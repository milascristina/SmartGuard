<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

// Variabila reactiva pentru a stoca toate datele din formular
const form = ref({
  username: '',
  passwordHash: '', // Retineti: trimitem parola in acest camp
  firstName: '',
  lastName: '',
  email: '',
  age: null,
  weightKg: null,
  heightCm: null,
  avgRestingHeartRate: null, // Poate fi nul la inregistrare
  lastSpO2: null,           // Poate fi nul la inregistrare
  dailyStepsGoal: 10000
});

const successMessage = ref('');
const errorMessage = ref('');

const handleRegister = async () => {
  successMessage.value = '';
  errorMessage.value = '';

  // Validare simpla
  if (!form.value.username || !form.value.passwordHash || !form.value.email) {
    errorMessage.value = 'Vă rugăm să completați câmpurile obligatorii (Utilizator, Parolă, Email).';
    return;
  }

  try {
    // Cererea POST către endpoint-ul Spring Boot
    const response = await axios.post('/api/auth/register/user', form.value);

    if (response.status === 201) {
      successMessage.value = 'Înregistrare reușită! Puteți să vă autentificați acum.';

      // Redirectionare catre pagina de login dupa succes
      setTimeout(() => {
        router.push('/');
      }, 2000);
    }
  } catch (error) {
    console.error('Eroare la înregistrare:', error.response ? error.response.data : error.message);

    if (error.response && error.response.status === 400) {
      errorMessage.value = 'Datele trimise sunt invalide (ex: Utilizatorul sau Emailul există deja).';
    } else {
      errorMessage.value = 'Eroare de rețea sau server. Vă rugăm să încercați din nou.';
    }
  }
};
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-container large-container">
      <h2 class="title">Monitorizare Sănătate</h2>
      <p class="subtitle">Înregistrare Pacient</p>

      <form @submit.prevent="handleRegister" class="auth-form">

        <div class="form-row">
          <div class="form-group">
            <label for="username">Utilizator (Login)</label>
            <input type="text" id="username" v-model="form.username" required class="input-field" />
          </div>
          <div class="form-group">
            <label for="passwordHash">Parolă</label>
            <input type="password" id="passwordHash" v-model="form.passwordHash" required class="input-field" />
          </div>
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="form.email" required class="input-field" />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="firstName">Prenume</label>
            <input type="text" id="firstName" v-model="form.firstName" required class="input-field" />
          </div>
          <div class="form-group">
            <label for="lastName">Nume de Familie</label>
            <input type="text" id="lastName" v-model="form.lastName" required class="input-field" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group small-input">
            <label for="age">Vârstă</label>
            <input type="number" id="age" v-model.number="form.age" required class="input-field" min="1" />
          </div>
          <div class="form-group small-input">
            <label for="weightKg">Greutate (kg)</label>
            <input type="number" id="weightKg" v-model.number="form.weightKg" required class="input-field" step="0.1" min="1" />
          </div>
          <div class="form-group small-input">
            <label for="heightCm">Înălțime (cm)</label>
            <input type="number" id="heightCm" v-model.number="form.heightCm" required class="input-field" min="1" />
          </div>
        </div>

        <div class="form-group">
          <label for="dailyStepsGoal">Obiectiv zilnic (Pași)</label>
          <input type="number" id="dailyStepsGoal" v-model.number="form.dailyStepsGoal" required class="input-field" min="1000" />
        </div>


        <button type="submit" class="btn-submit">Înregistrează-te</button>

        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

        <p class="register-link">
          Ai deja cont?
          <router-link to="/">Autentifică-te</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* Stilurile sunt reutilizate de la Login.vue pentru uniformitate */
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30px 0; /* Adaugam padding pentru a permite vizualizarea pe ecrane mai mici */
}
.large-container {
  width: 650px; /* Latime mai mare pentru formularul de inregistrare */
}
.auth-container {
  background-color: var(--color-background-soft);
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
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

/* Stiluri Grid pentru a așeza câmpurile pe rânduri */
.form-row {
  display: flex;
  gap: 20px;
}
.form-row .form-group {
  flex: 1; /* Distribuie spatiul egal intre grupuri */
}

.small-input {
  min-width: 100px; /* Asigura latime minima pentru campurile numerice */
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
  background-color: var(--color-accent);
  color: white;
  font-size: 1.1em;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
}
.btn-submit:hover {
  background-color: var(--color-accent-hover);
}
.error-message {
  color: #ff8a80;
  margin-top: 15px;
  text-align: center;
}
.success-message {
  color: #8aff80; /* Verde deschis pentru succes */
  margin-top: 15px;
  text-align: center;
}
.register-link {
  margin-top: 20px;
  text-align: center;
  font-size: 0.9em;
}
</style>