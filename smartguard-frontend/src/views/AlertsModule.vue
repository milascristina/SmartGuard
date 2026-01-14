<template>
  <div class="page-wrapper">
    <nav class="navigation-bar">
      <button @click="router.push('/main')" class="btn-back-clean">
        <span class="arrow">←</span> Panou Principal
      </button>
    </nav>

    <div class="content-container">
      <header class="section-header">
        <h1 class="title">Evaluare & Alerte AI</h1>
        <p class="subtitle">Monitorizare inteligentă în timp real pentru parametrii tăi vitali.</p>
      </header>

      <div class="alerts-grid">
        <section class="glass-card status-banner" :class="systemStatus.colorClass">
          <div class="status-info">
            <div class="status-title-group">
              <span class="status-icon">{{ systemStatus.icon }}</span>
              <h2>Status Sistem</h2>
            </div>
            <p class="status-message">{{ systemStatus.message }}</p>
          </div>
          <div class="progress-container">
            <div class="progress-label">Integritate Parametri: {{ systemStatus.score }}%</div>
            <div class="progress-bar-bg">
              <div class="progress-fill" :style="{ width: systemStatus.score + '%' }"></div>
            </div>
          </div>
        </section>

        <section class="glass-card alerts-feed">
          <div class="card-header-row">
            <h3>Flux Alerte Active</h3>
            <span class="alert-count">{{ alerts.length }}</span>
          </div>

          <div v-if="alerts.length > 0" class="alerts-list">
            <transition-group name="fade-list">
              <div v-for="alert in alerts" :key="alert.id" :class="['alert-box', alert.severity]">
                <div class="alert-body">
                  <div class="alert-meta">
                    <span class="severity-pill">{{ alert.severity.toUpperCase() }}</span>
                    <span class="alert-time">{{ formatDate(alert.timestamp) }}</span>
                  </div>
                  <h4>{{ alert.title }}</h4>
                  <p>{{ alert.description }}</p>
                </div>
                <button @click="dismissAlert(alert.id)" class="btn-confirm">Confirmă</button>
              </div>
            </transition-group>
          </div>

          <div v-else class="empty-state">
            <div class="success-icon">✅</div>
            <p>Toți parametrii sunt optimi.</p>
            <small>Nu au fost detectate anomalii în ultimele măsurători.</small>
          </div>
        </section>

        <aside class="logic-sidebar">
          <div class="glass-card logic-card">
            <h3>Parametri Monitorizați</h3>
            <div class="logic-items">
              <div class="logic-pill">
                <strong>Ritm Cardiac</strong>
                <span class="threshold">> 100 BPM în repaus</span>
              </div>
              <div class="logic-pill">
                <strong>Greutate (57kg)</strong>
                <span class="threshold">Scăderi bruste detectate</span>
              </div>
              <div class="logic-pill">
                <strong>Oboseală Maraton</strong>
                <span class="threshold">Nivel Energie &lt; 3</span>
              </div>
            </div>
          </div>

          <div class="ai-notice">
            <span class="notice-icon">ℹ️</span>
            <p>Sistemul AI corelează automat jurnalul tău de energie cu datele senzorilor pentru a detecta supra-antrenamentul.</p>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const alerts = ref([
  {
    id: 1,
    title: "Tahicardie ușoară detectată",
    description: "Ritmul cardiac a depășit 105 BPM în repaus. Recomandăm hidratare suplimentară.",
    severity: "warning",
    timestamp: new Date().toISOString()
  },
  {
    id: 2,
    title: "Deficit caloric probabil",
    description: "Nivel energie raportat: 2/10. Verifică aportul de nutrienți pentru maraton.",
    severity: "critical",
    timestamp: new Date().toISOString()
  }
]);

const systemStatus = ref({
  message: "Analiza curentă indică necesitatea unor ajustări minore.",
  icon: "⚠️",
  colorClass: "status-warning",
  score: 65
});

const formatDate = (isoString) => {
  return new Date(isoString).toLocaleString('ro-RO', { hour: '2-digit', minute: '2-digit' });
};

const dismissAlert = (id) => {
  alerts.value = alerts.value.filter(a => a.id !== id);
  if (alerts.value.length === 0) {
    systemStatus.value = {
      message: "Sistem optim. Parametrii tăi sunt în limite ideale.",
      icon: "✅",
      colorClass: "status-ok",
      score: 100
    };
  }
};
</script>

<style scoped>
/* Reset & Theme - Aliniere cu Gestiune Date și Analiză */
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

/* Grid Alerte */
.alerts-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.glass-card {
  background: #16161a;
  border: 1px solid #2d2d35;
  border-radius: 24px;
  padding: 25px;
}

/* Status Banner */
.status-banner {
  grid-column: 1 / -1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: none;
  padding: 30px 40px;
}

.status-warning { background: linear-gradient(135deg, #f59e0b, #d97706); }
.status-ok { background: linear-gradient(135deg, #10b981, #059669); }
.status-critical { background: linear-gradient(135deg, #ef4444, #dc2626); }

.status-title-group { display: flex; align-items: center; gap: 15px; margin-bottom: 10px; }
.status-icon { font-size: 2rem; }
.status-message { font-size: 1.1rem; opacity: 0.9; }

.progress-container { width: 300px; }
.progress-label { font-size: 0.8rem; margin-bottom: 8px; font-weight: bold; }
.progress-bar-bg { background: rgba(255, 255, 255, 0.2); height: 8px; border-radius: 4px; overflow: hidden; }
.progress-fill { background: #fff; height: 100%; transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1); }

/* Alerts Feed */
.card-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
.alert-count { background: #ef4444; color: white; padding: 2px 12px; border-radius: 20px; font-weight: bold; font-size: 0.9rem; }

.alerts-list { display: flex; flex-direction: column; gap: 20px; }

.alert-box {
  background: #0f172a;
  border-radius: 18px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-left: 6px solid #334155;
}

.alert-box.warning { border-left-color: #f59e0b; }
.alert-box.critical { border-left-color: #ef4444; }

.alert-meta { display: flex; gap: 10px; align-items: center; margin-bottom: 8px; }
.severity-pill { font-size: 0.7rem; font-weight: 800; padding: 2px 8px; border-radius: 4px; background: rgba(255, 255, 255, 0.1); }
.alert-time { font-size: 0.8rem; color: #64748b; }

.alert-body h4 { margin-bottom: 5px; font-size: 1.1rem; }
.alert-body p { font-size: 0.95rem; color: #94a3b8; margin: 0; line-height: 1.5; }

.btn-confirm {
  background: #1e293b;
  color: white;
  border: 1px solid #334155;
  padding: 10px 20px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.2s;
}

.btn-confirm:hover { background: #334155; border-color: #475569; }

/* Logic Sidebar */
.logic-items { display: flex; flex-direction: column; gap: 15px; margin-top: 20px; }
.logic-pill {
  background: #0f172a;
  padding: 15px;
  border-radius: 14px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.logic-pill strong { color: #fff; font-size: 0.95rem; }
.threshold { color: #a855f7; font-size: 0.85rem; font-weight: 600; }

.ai-notice {
  margin-top: 25px;
  padding: 20px;
  background: rgba(168, 85, 247, 0.05);
  border: 1px dashed rgba(168, 85, 247, 0.3);
  border-radius: 18px;
  display: flex;
  gap: 12px;
}

.notice-icon { font-size: 1.2rem; }
.ai-notice p { font-size: 0.85rem; color: #94a3b8; margin: 0; line-height: 1.5; }

.empty-state { text-align: center; padding: 60px 20px; color: #64748b; }
.success-icon { font-size: 3rem; margin-bottom: 15px; }

@media (max-width: 1000px) {
  .alerts-grid { grid-template-columns: 1fr; }
}

/* Animations */
.fade-list-enter-active, .fade-list-leave-active { transition: all 0.4s ease; }
.fade-list-enter-from, .fade-list-leave-to { opacity: 0; transform: translateY(20px); }
</style>