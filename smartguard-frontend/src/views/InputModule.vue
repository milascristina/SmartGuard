<template>
  <div class="page-wrapper">
    <nav class="navigation-bar">
      <button @click="router.push('/main')" class="btn-back-clean">
        <span class="arrow">←</span> Panou Principal
      </button>
    </nav>

    <div class="content-container">
      <header class="section-header">
        <h1 class="title">Centralizare Date</h1>
        <p class="subtitle">Sincronizează dispozitivele sau înregistrează manual progresul tău zilnic.</p>
      </header>

      <div class="interactive-grid">
        <section class="glass-card">
          <div class="card-icon">⌚</div>
          <div class="card-content">
            <h3>Import Smartwatch</h3>
            <p>Încarcă istoricul activității (Ritm Cardiac & Pași) în format .CSV</p>

            <div class="upload-zone">
              <input
                  type="file"
                  @change="handleFileUpload"
                  accept=".csv"
                  ref="fileInput"
                  id="file-upload"
                  hidden
              />
              <label for="file-upload" class="custom-file-label">
                {{ selectedFile ? selectedFile.name : 'Selectează fișierul...' }}
              </label>

              <button
                  @click="uploadFile"
                  :disabled="!selectedFile"
                  class="btn-action btn-green"
              >
                Procesează Importul
              </button>
            </div>
          </div>
        </section>

        <section class="glass-card">
          <div class="card-icon">📝</div>
          <div class="card-content">
            <h3>Jurnal Personal</h3>
            <form @submit.prevent="saveManualEntry" class="entry-form">
              <div class="input-group">
                <div class="label-row">
                  <label>Nivel Energie</label>
                  <span class="energy-badge">{{ entry.energyLevel }}/10</span>
                </div>
                <input
                    v-model.number="entry.energyLevel"
                    type="range"
                    min="1"
                    max="10"
                    class="styled-range"
                />
              </div>

              <div class="input-group">
                <label>Note Nutriție & Antrenament</label>
                <textarea
                    v-model="entry.notes"
                    placeholder="Ex: Alergare 15km, mic dejun cu ovăz și banane."
                    rows="4"
                ></textarea>
              </div>

              <button type="submit" class="btn-action btn-purple">
                Salvează Jurnalul
              </button>
            </form>
          </div>
        </section>
      </div>

      <transition name="slide-up">
        <div v-if="message" :class="['toast-msg', isError ? 'msg-error' : 'msg-success']">
          {{ isError ? '🛑' : '✅' }} {{ message }}
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const selectedFile = ref(null);
const fileInput = ref(null);
const message = ref('');
const isError = ref(false);

const entry = ref({ energyLevel: 5, notes: '' });

const handleFileUpload = (event) => {
  selectedFile.value = event.target.files[0];
};

const showMessage = (text, error = false) => {
  message.value = text;
  isError.value = error;
  setTimeout(() => { message.value = ''; }, 4000);
};

const uploadFile = async () => {
  const userId = localStorage.getItem('userId');
  const formData = new FormData();
  formData.append('file', selectedFile.value);

  try {
    await axios.post(`/api/data/upload/sensor?userId=${userId}`, formData);
    showMessage("Datele au fost importate cu succes!");
    selectedFile.value = null;
  } catch (err) {
    showMessage("Eroare la procesarea fișierului.", true);
  }
};

const saveManualEntry = async () => {
  const userId = localStorage.getItem('userId');
  try {
    await axios.post(`/api/data/manual/entry?userId=${userId}`, {
      ...entry.value,
      timestamp: new Date().toISOString(),
      entryType: 'GENERIC'
    });
    showMessage("Înregistrarea a fost salvată!");
    entry.value = { energyLevel: 5, notes: '' };
  } catch (err) {
    showMessage("Eroare la salvarea datelor.", true);
  }
};
</script>

<style scoped>
/* Reset & Layout */
.page-wrapper {
  min-height: 100vh;
  background-color: #0a0a0b; /* Fundal ultra-dark pentru contrast maxim */
  color: #ffffff;
  padding-bottom: 50px;
}

.navigation-bar {
  padding: 20px 5%;
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.content-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Header */
.section-header {
  text-align: center;
  margin: 40px 0;
}

.title {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 10px;
  background: linear-gradient(to right, #ffffff, #a855f7);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  color: #94a3b8;
  font-size: 1.1rem;
}

/* Interactive Cards Grid */
.interactive-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(380px, 1fr));
  gap: 30px;
  margin-top: 20px;
}

.glass-card {
  background: #16161a;
  border: 1px solid #2d2d35;
  border-radius: 24px;
  padding: 30px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  transition: transform 0.3s, border-color 0.3s;
}

.glass-card:hover {
  transform: translateY(-5px);
  border-color: #a855f7;
}

.card-icon {
  font-size: 2.5rem;
  background: rgba(255, 255, 255, 0.05);
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16px;
}

.card-content h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
  color: #ffffff;
}

.card-content p {
  color: #94a3b8;
  line-height: 1.6;
}

/* Form Elements */
.upload-zone {
  margin-top: 25px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.custom-file-label {
  display: block;
  padding: 15px;
  background: #0f172a;
  border: 2px dashed #334155;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  color: #cbd5e1;
}

.input-group {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.label-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.energy-badge {
  background: #a855f7;
  padding: 2px 10px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 0.9rem;
}

textarea {
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 12px;
  padding: 15px;
  color: white;
  font-family: inherit;
  resize: none;
}

textarea:focus {
  border-color: #a855f7;
  outline: none;
}

/* Buttons */
.btn-action {
  padding: 14px;
  border: none;
  border-radius: 12px;
  font-weight: 700;
  font-size: 1rem;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.1s;
}

.btn-green { background: #10b981; color: white; }
.btn-purple { background: #a855f7; color: white; }

.btn-action:hover { opacity: 0.9; }
.btn-action:active { transform: scale(0.98); }
.btn-action:disabled { background: #334155; cursor: not-allowed; }

.btn-back-clean {
  background: transparent;
  border: 1px solid #475569;
  color: #ffffff;
  padding: 8px 16px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
}

.btn-back-clean:hover {
  border-color: #ffffff;
}

/* Toasts */
.toast-msg {
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  padding: 16px 32px;
  border-radius: 50px;
  font-weight: bold;
  z-index: 1000;
  box-shadow: 0 10px 25px rgba(0,0,0,0.5);
}

.msg-success { background: #10b981; color: white; }
.msg-error { background: #ef4444; color: white; }

/* Styles for Range Input */
.styled-range {
  width: 100%;
  accent-color: #a855f7;
}

@media (max-width: 600px) {
  .interactive-grid { grid-template-columns: 1fr; }
}
</style>