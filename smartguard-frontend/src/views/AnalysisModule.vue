<template>
  <div class="page-wrapper">
    <nav class="navigation-bar">
      <button @click="router.push('/main')" class="btn-back-clean">
        <span class="arrow">←</span> Panou Principal
      </button>
    </nav>

    <div class="content-container">
      <header class="section-header">
        <h1 class="title">Analiza Performanței</h1>
        <p class="subtitle">Monitorizarea biometrică și progresul către obiectivele tale.</p>
      </header>

      <div class="analysis-grid">
        <section class="glass-card main-chart-area">
          <div class="card-header-row">
            <div class="title-group">
              <span class="icon">📈</span>
              <h3>Ritm Cardiac (BPM)</h3>
            </div>
            <button @click="fetchData" class="btn-refresh-pulse">🔄 Actualizează Date</button>
          </div>

          <div class="chart-container">
            <Line v-if="loaded && chartData.labels.length > 0" :data="chartData" :options="chartOptions" />

            <div v-else-if="!loaded" class="chart-overlay">
              <div class="premium-spinner"></div>
              <p>Sincronizare cu baza de date...</p>
            </div>

            <div v-else class="chart-overlay empty">
              <p>📭 Nu au fost găsite înregistrări recente.</p>
              <small>Încarcă un fișier CSV în secțiunea Gestiune Date.</small>
            </div>
          </div>
        </section>

        <aside class="stats-sidebar">
          <div class="glass-card compact">
            <h3>Parametri Profil</h3>
            <div class="stat-rows">
              <div class="stat-pill green">
                <span class="label">Greutate</span>
                <span class="value">57 kg</span>
              </div>
              <div class="stat-pill blue">
                <span class="label">Înălțime</span>
                <span class="value">1.67 m</span>
              </div>
              <div class="stat-pill purple">
                <span class="label">Obiectiv</span>
                <span class="value">Maraton</span>
              </div>
            </div>
          </div>

          <div class="ai-insight-card">
            <div class="ai-header">
              <span class="ai-icon">🤖</span>
              <h4>Insight Inteligență Artificială</h4>
            </div>
            <div class="ai-content">
              <p v-if="avgBpm > 80">
                Ritmul tău cardiac mediu este ridicat. <strong>Recomandare:</strong> Crește hidratarea și odihna înainte de următoarea sesiune.
              </p>
              <p v-else>
                Performanța ta este excelentă. Ritmul cardiac stabil indică o pregătire optimă pentru cursă.
              </p>
            </div>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {Line} from 'vue-chartjs';
import {
  Chart as ChartJS, Title, Tooltip, Legend, LineElement,
  CategoryScale, LinearScale, PointElement, Filler
} from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement, Filler);

const router = useRouter();
const loaded = ref(false);
const avgBpm = ref(0);
const chartData = ref({labels: [], datasets: []});

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {display: false},
    tooltip: {
      backgroundColor: '#16161a',
      titleColor: '#a855f7',
      bodyColor: '#fff',
      borderColor: '#334155',
      borderWidth: 1,
      padding: 12,
      displayColors: false
    }
  },
  scales: {
    y: {
      grid: {color: 'rgba(255, 255, 255, 0.05)'},
      ticks: {color: '#94a3b8', font: {size: 12}}
    },
    x: {
      grid: {display: false},
      ticks: {color: '#94a3b8', font: {size: 11}}
    }
  }
};

const fetchData = async () => {
  loaded.value = false;
  try {
    const userId = localStorage.getItem('userId');
    const response = await axios.get(`/api/data/history/sensor/HEART_RATE?userId=${userId}`);

    if (response.data && response.data.length > 0) {
      const sorted = [...response.data].sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
      processData(sorted);
    } else {
      generateMockData();
    }
  } catch (error) {
    generateMockData();
  }
  loaded.value = true;
};

const processData = (data) => {
  const sum = data.reduce((a, b) => a + (b.sensorValue || b.value), 0);
  avgBpm.value = sum / data.length;

  chartData.value = {
    labels: data.map(d => new Date(d.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})),
    datasets: [{
      label: 'BPM',
      data: data.map(d => d.sensorValue || d.value),
      borderColor: '#a855f7',
      backgroundColor: 'rgba(168, 85, 247, 0.15)',
      fill: true,
      tension: 0.4,
      borderWidth: 3,
      pointRadius: 0,
      pointHoverRadius: 6
    }]
  };
};

const generateMockData = () => {
  chartData.value = {
    labels: ['08:00', '10:00', '12:00', '14:00', '16:00'],
    datasets: [{
      label: 'BPM (Demo)',
      data: [68, 72, 85, 74, 69],
      borderColor: '#10b981',
      backgroundColor: 'rgba(16, 185, 129, 0.1)',
      fill: true,
      tension: 0.4,
      borderWidth: 2
    }]
  };
};

onMounted(fetchData);
</script>

<style scoped>
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

.btn-back-clean:hover {
  border-color: #a855f7;
  color: #a855f7;
}

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

.subtitle {
  color: #94a3b8;
  margin-top: 10px;
}

/* Grid Analysis */
.analysis-grid {
  display: grid;
  grid-template-columns: 2.5fr 1fr;
  gap: 30px;
}

.glass-card {
  background: #16161a;
  border: 1px solid #2d2d35;
  border-radius: 24px;
  padding: 25px;
}

.main-chart-area {
  min-height: 450px;
  display: flex;
  flex-direction: column;
}

.card-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.title-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-group h3 {
  font-size: 1.3rem;
  margin: 0;
}

.chart-container {
  flex-grow: 1;
  position: relative;
  min-height: 350px;
}

.chart-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(10, 10, 11, 0.7);
  border-radius: 20px;
  text-align: center;
}

.chart-overlay.empty {
  color: #64748b;
}

/* Sidebar Stats */
.stats-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stat-rows {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 20px;
}

.stat-pill {
  display: flex;
  justify-content: space-between;
  padding: 12px 20px;
  border-radius: 14px;
  background: #0f172a;
  border: 1px solid #1e293b;
}

.stat-pill.green {
  border-left: 4px solid #10b981;
}

.stat-pill.blue {
  border-left: 4px solid #3b82f6;
}

.stat-pill.purple {
  border-left: 4px solid #a855f7;
}

.stat-pill .label {
  color: #94a3b8;
  font-size: 0.9rem;
}

.stat-pill .value {
  font-weight: 700;
  color: #fff;
}

.ai-insight-card {
  background: linear-gradient(135deg, rgba(168, 85, 247, 0.1), rgba(168, 85, 247, 0.05));
  border: 1px solid rgba(168, 85, 247, 0.2);
  padding: 20px;
  border-radius: 20px;
}

.ai-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.ai-icon {
  font-size: 1.4rem;
}

.ai-header h4 {
  margin: 0;
  color: #a855f7;
}

.ai-content p {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #cbd5e1;
  margin: 0;
}

/* Animations */
.premium-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(168, 85, 247, 0.1);
  border-top-color: #a855f7;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 1000px) {
  .analysis-grid {
    grid-template-columns: 1fr;
  }
}
</style>