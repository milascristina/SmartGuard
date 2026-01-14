# 🩺 SmartGuard | Sistem Inteligent de Monitorizare a Sănătății

SmartGuard este o platformă de monitorizare a sănătății care utilizează date agregate de la dispozitive purtabile (smartwatch) și intrări manuale pentru a genera o evaluare inteligentă a stării de bine, oferind alerte timpurii și facilitând comunicarea cu medicul curant.

---

## 🚀 Funcționalități Cheie

Proiectul este împărțit în patru module principale care gestionează întregul ciclu de viață al datelor de sănătate: de la colectare la analiză și alertare.

### 1. Modulul de Încărcare și Gestiune a Datelor (Input)

Acest modul asigură modalități flexibile și securizate de a introduce informațiile de sănătate.

#### 📥 Încărcare Date (Data Ingestion)
* **Import CSV/JSON:** Permite utilizatorului să încarce fișiere (simulând datele agregate de la smartwatch) care conțin serii de timp pentru Ritm Cardiac, Pași, Somn, etc.
* **Validare la Import:** Verificarea integrității datelor încărcate (ex: formatul datei/orei este corect, valorile ritmului cardiac sunt în intervalul realist).

#### 📝 Intrare Manuală
* **Jurnal de Stare:** Formular simplu pentru introducerea manuală a stării de spirit, a nivelului de energie sau a simptomelor (ex: "Durere de cap", "Oboseală").
* **Înregistrare Tensiune Arterială/Glicemie:** Permite introducerea manuală a măsurătorilor ocazionale.

#### 🧑‍🦱 Managementul Profilului Biometric
* Setarea și actualizarea detaliilor personale necesare pentru calcule (vârstă, greutate, înălțime, sex).

---

### 2. Modulul de Vizualizare și Analiză (Core)

Acesta transformă datele brute în informații utile, ușor de înțeles și accesat.

#### 📊 Tablou de Bord (Dashboard)
Afișarea sumară și agregată a ultimelor date introduse:
* Media ritmului cardiac de repaus pe ultimele 7 zile.
* Total pași / Progresul către obiectivul zilnic.
* Scorul mediu de somn.

#### 📈 Vizualizare Grafică Interactivă
* **Grafice de Tendințe:** Reprezentarea grafică a seriilor de date (Ritm Cardiac, Pași, Calorii) pe perioade selectabile (Zi, Săptămână, Lună).
* **Analiza Somnului:** Grafic ce arată fazele somnului (Ușor, Profund, REM) și durata totală.

#### 🧮 Calcularea Indicatorilor Avansați
* **Nivel de Activitate:** Calcularea minutele de efort moderat și intens.
* **Rata Metabolică Bazală (BMR) și Calorii Arse:** Calcularea caloriilor arse pe baza datelor biometrice și a datelor de activitate introduse.

---

### 3. Modulul de Evaluare și Alertare (AI/Smart Logic)

Acest modul definește caracterul "inteligent" al aplicației, oferind feedback proactiv bazat pe analiza datelor.

#### 🚨 Sistem de Praguri (Thresholding)
* Definirea unor praguri critice (ex: Ritm cardiac de repaus peste 100 bpm sau sub 45 bpm; SpO2 sub 90%).

#### 🧠 Logica AI Simplificată (Alerte)
Funcția dumneavoastră de AI va analiza datele încărcate și va returna un rezultat de alertă dacă:
* Un indicator critic a fost depășit.
* S-a detectat o tendință negativă (ex: Ritmul cardiac de repaus a crescut constant cu 10% pe o perioadă de 3 zile).

#### 🔔 Sistem de Alerte și Notificări
* **Alerte în Aplicație:** Afișarea unui mesaj vizibil (roșu) pe Dashboard dacă funcția AI a returnat o alertă.
* **Istoric Alerte:** Păstrarea unui jurnal al tuturor alertelor generate.
* **Feedback Obiective:** Notificări pozitive atunci când obiectivele (pași, somn) sunt atinse.

---

### 4. Modulul de Interacțiune Doctor/Pacient

Acest modul utilizează relațiile dintre utilizatori și medici implementate în structura bazei de date.

#### 🤝 Căutare și Cerere Medic (Pentru Pacient)
* Funcționalitate pentru User de a căuta un Doctor după specializare sau nume.
* Trimiterea cererii de monitorizare către Doctor (Status: `PENDING`).

#### 👨‍⚕️ Dashboard Doctor
* Vizualizarea listei de **Cereri în Așteptare (`PENDING`)** de la pacienți.
* Opțiunea de a **Accepta** sau **Respinge** cererea (schimbând starea relației).

#### 🔗 Partajarea Datelor
* Odată ce relația este **`ACCEPTED`**, Doctorul poate vedea tabloul de bord al pacientului și istoricul de alerte, asigurând monitorizarea de la distanță.
