import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  // 1. ADAUGAREA BLOCULUI SERVER PENTRU A REZOLVA PROBLEMA 404/CORS
  server: {
    proxy: {
      // Cand Vue face un request catre /api, acesta este trimis la Spring Boot
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false, // Folosit pentru conexiuni HTTP locale
      }
    }
  },
  // 2. RESTUL CONFIGURATIEI RAMANE LA FEL
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})