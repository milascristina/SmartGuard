<template>
  <div class="page-wrapper">
    <nav class="navigation-bar">
      <button @click="router.push('/main')" class="btn-back-clean">
        <span class="arrow">←</span> Panou Principal
      </button>
    </nav>

    <div class="content-container">
      <header class="section-header">
        <h1 class="title">Asistență Medicală</h1>
        <p class="subtitle">Conectează-te cu specialiști pentru monitorizarea obiectivelor tale de performanță.</p>
      </header>

      <div class="doctor-grid-layout">
        <section class="search-and-results">
          <div class="glass-card search-card">
            <div class="search-input-wrapper">
              <span class="search-icon">🔍</span>
              <input
                  v-model="searchQuery"
                  type="text"
                  placeholder="Caută medic după nume sau specializare..."
              />
            </div>
          </div>

          <div class="doctors-stack">
            <div v-if="filteredDoctors.length > 0" class="cards-wrapper">
              <div v-for="doc in filteredDoctors" :key="doc.id" class="glass-card doc-entry">
                <div class="doc-header">
                  <div class="doc-avatar">🩺</div>
                  <div class="doc-info-meta">
                    <span class="doc-tag">{{ doc.specialization }}</span>
                    <h3>Dr. {{ doc.firstName }} {{ doc.lastName }}</h3>
                  </div>
                </div>

                <p class="doc-bio">
                  Expert în monitorizarea datelor biometrice și optimizarea antrenamentului pentru maraton.
                </p>

                <button
                    @click="sendDataRequest(doc)"
                    :disabled="doc.requested"
                    :class="['btn-action', doc.requested ? 'btn-disabled' : 'btn-green']"
                >
                  <span v-if="doc.requested">✅ Cerere trimisă</span>
                  <span v-else>Partajează datele mele</span>
                </button>
              </div>
            </div>

            <div v-else class="empty-results">
              <p>Nu am găsit medici care să corespundă căutării.</p>
            </div>
          </div>
        </section>

        <aside class="doctor-sidebar">
          <div class="glass-card status-box">
            <h4 class="sidebar-title">Conexiune Activă</h4>
            <div class="connection-display" :class="{ 'connected': activeDoctor }">
              <div v-if="activeDoctor">
                <div class="live-pulse"></div>
                <p>Monitorizat de:</p>
                <strong>Dr. {{ activeDoctor.name }}</strong>
                <button @click="activeDoctor = null" class="btn-disconnect">Întrerupe accesul</button>
              </div>
              <div v-else class="disconnected-state">
                <p>Nicio conexiune stabilită</p>
                <small>Datele tale sunt momentan private.</small>
              </div>
            </div>
          </div>

          <div class="glass-card profile-preview">
            <h4 class="sidebar-title">Profil Partajat</h4>
            <div class="mini-stats">
              <div class="mini-pill green">Greutate: 57 kg</div>
              <div class="mini-pill blue">Înălțime: 1.67 m</div>
              <div class="mini-pill purple">Scop: Maraton</div>
            </div>
            <div class="ai-warning-box">
              <small>ℹ️ Medicul va avea acces la alertele tale AI și la jurnalul tău energetic.</small>
            </div>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const searchQuery = ref('');
const activeDoctor = ref(null);

const doctors = ref([
  { id: 1, firstName: "Andrei", lastName: "Ionescu", specialization: "Cardiologie Sportivă", requested: false },
  { id: 2, firstName: "Elena", lastName: "Dumitrescu", specialization: "Nutriție & Dietetică", requested: false },
  { id: 3, firstName: "Mihai", lastName: "Vasilescu", specialization: "Fiziokinetoterapie", requested: false }
]);

const filteredDoctors = computed(() => {
  return doctors.value.filter(doc =>
      doc.lastName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      doc.specialization.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const sendDataRequest = (doc) => {
  doc.requested = true;
  setTimeout(() => {
    activeDoctor.value = { name: doc.lastName };
  }, 1500);
};
</script>

<style scoped>
/* Reset & Layout Premium */
.page-wrapper {
  min-height: 100vh;
  background-color: #0a0a0b;
  color: #ffffff;
  padding-bottom: 60px;
}

.navigation-bar {
  padding: 20px 5%;
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.btn-back-clean {
  background: transparent;
  border: 1px solid #475569;
  color: #ffffff;
  padding: 8px 18px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  transition: 0.3s;
}

.btn-back-clean:hover { border-color: #a855f7; color: #a855f7; }

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-header {
  text-align: center;
  margin: 40px 0;
}

.title {
  font-size: 2.5rem;
  font-weight: 800;
  background: linear-gradient(to right, #ffffff, #a855f7);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle { color: #94a3b8; margin-top: 10px; }

/* Grid Layout */
.doctor-grid-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.glass-card {
  background: #16161a;
  border: 1px solid #2d2d35;
  border-radius: 24px;
  padding: 25px;
  margin-bottom: 25px;
}

/* Search Card */
.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon { position: absolute; left: 15px; opacity: 0.5; }

.search-input-wrapper input {
  width: 100%;
  padding: 14px 14px 14px 45px;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 14px;
  color: white;
  font-size: 1rem;
}

/* Doctor Cards */
.doctors-stack { display: flex; flex-direction: column; }

.doc-entry { transition: transform 0.3s, border-color 0.3s; }
.doc-entry:hover { transform: translateY(-5px); border-color: #a855f7; }

.doc-header { display: flex; gap: 15px; align-items: center; margin-bottom: 15px; }

.doc-avatar {
  width: 55px; height: 55px;
  background: #2a2a2a;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 1.5rem;
}

.doc-tag {
  font-size: 0.75rem;
  font-weight: bold;
  color: #a855f7;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.doc-bio { color: #94a3b8; font-size: 0.95rem; line-height: 1.6; margin-bottom: 20px; }

/* Buttons */
.btn-action {
  width: 100%;
  padding: 12px;
  border-radius: 12px;
  border: none;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}

.btn-green { background: #10b981; color: white; }
.btn-disabled { background: #334155; color: #94a3b8; cursor: default; }

/* Sidebar */
.sidebar-title {
  font-size: 0.85rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  margin-bottom: 20px;
}

.connection-display {
  background: #0f172a;
  border: 1px dashed #334155;
  border-radius: 20px;
  padding: 25px;
  text-align: center;
}

.connection-display.connected {
  border: 2px solid #10b981;
  background: rgba(16, 185, 129, 0.05);
}

.live-pulse {
  width: 10px; height: 10px;
  background: #10b981;
  border-radius: 50%;
  margin: 0 auto 10px;
  box-shadow: 0 0 10px #10b981;
}

.btn-disconnect {
  margin-top: 15px;
  background: transparent;
  border: 1px solid #ef4444;
  color: #ef4444;
  padding: 5px 12px;
  border-radius: 8px;
  font-size: 0.8rem;
  cursor: pointer;
}

/* Mini Stats */
.mini-stats { display: flex; flex-direction: column; gap: 10px; margin-bottom: 15px; }

.mini-pill {
  padding: 10px 15px;
  border-radius: 12px;
  background: #0f172a;
  font-size: 0.9rem;
  font-weight: 600;
}

.mini-pill.green { border-left: 4px solid #10b981; }
.mini-pill.blue { border-left: 4px solid #3b82f6; }
.mini-pill.purple { border-left: 4px solid #a855f7; }

.ai-warning-box {
  padding: 12px;
  background: rgba(168, 85, 247, 0.05);
  border-radius: 10px;
  color: #94a3b8;
  font-style: italic;
}

@media (max-width: 900px) {
  .doctor-grid-layout { grid-template-columns: 1fr; }
}
</style>