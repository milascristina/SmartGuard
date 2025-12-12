package org.example.smartguard.Services;

import org.example.smartguard.Model.User;
import org.example.smartguard.Model.SensorData;
import org.example.smartguard.Model.ManualEntry;
import org.example.smartguard.Model.DataType;
import org.example.smartguard.Model.EntryType;
import org.example.smartguard.Repository.SensorDataRepository;
import org.example.smartguard.Repository.ManualEntryRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataIngestionService {

    // --- Injectare prin Constructor ---
    private final SensorDataRepository sensorDataRepository;
    private final ManualEntryRepository manualEntryRepository;
    private final UserService userService;

    public DataIngestionService(SensorDataRepository sensorDataRepository,
                                ManualEntryRepository manualEntryRepository,
                                UserService userService) {
        this.sensorDataRepository = sensorDataRepository;
        this.manualEntryRepository = manualEntryRepository;
        this.userService = userService;
    }

    // --- Metoda 1: Încărcarea datelor de la senzori (CSV/JSON) ---
    public void processSensorFile(MultipartFile file, Long userId) throws Exception {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizator negăsit: " + userId));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Fișierul încărcat este gol.");
        }

        List<SensorData> dataToSave = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                try {
                    DataType type = DataType.valueOf(parts[0].trim().toUpperCase());
                    Instant timestamp = Instant.parse(parts[1].trim());
                    Double value = Double.parseDouble(parts[2].trim());

                    if (type == DataType.HEART_RATE && (value < 30 || value > 220)) {
                        throw new IllegalArgumentException("Valoare ritm cardiac nerealistă: " + value);
                    }

                    SensorData data = new SensorData();
                    data.setUser(user);
                    data.setTimestamp(timestamp);
                    data.setType(type);
                    data.setValue(value);
                    dataToSave.add(data);

                } catch (IllegalArgumentException | java.time.format.DateTimeParseException e) {
                    System.err.println("Eroare de parsare/validare la linia " + lineNumber + ". Eroare: " + e.getMessage());
                }
            }
        }

        if (!dataToSave.isEmpty()) {
            sensorDataRepository.saveAll(dataToSave);
        } else if (lineNumber > 1) {
            throw new Exception("Nicio linie validă nu a fost găsită în fișier după parsare.");
        }
    }

    // --- Metoda 2: Salvarea intrărilor manuale (PRIMEȘTE ManualEntry în loc de DTO) ---
    /**
     * Salvează o înregistrare manuală (Jurnal, Tensiune, Glicemie) în ManualEntry.
     * @param entry Entitatea ManualEntry primită de la client.
     * @param userId ID-ul utilizatorului.
     * @return Entitatea ManualEntry salvată.
     */
    public ManualEntry saveManualEntry(ManualEntry entry, Long userId) { // <-- S-A SCHIMBAT AICI

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizator negăsit: " + userId));

        // 1. Mapare/Setare date esențiale (Supra-scriem User-ul și Timpul pentru securitate/coerență)
        entry.setUser(user);

        // Asigurăm data introducerii (sau putem păstra data din entitatea primită, dar data creării este mai sigură)
        entry.setEntryDate(LocalDateTime.now(ZoneOffset.UTC));

        // 2. Validare specifică (Folosim entry în loc de dto)
        if (entry.getType() == EntryType.BLOOD_PRESSURE) {
            if (entry.getSystolic() == null || entry.getDiastolic() == null) {
                throw new IllegalArgumentException("Tensiunea arterială necesită valori sistolice și diastolice.");
            }
            if (entry.getSystolic() < 50 || entry.getDiastolic() < 30) {
                throw new IllegalArgumentException("Valori nerealiste pentru tensiunea arterială.");
            }
        }

        // 3. Salvare în istoric
        return manualEntryRepository.save(entry);
    }

    // --- Metoda 3: Preluarea istoricului ---
    public List<SensorData> getSensorDataHistory(Long userId, DataType type) {
        return sensorDataRepository.findByUser_IdAndTypeOrderByTimestampDesc(userId, type);
    }

    public List<ManualEntry> getManualEntryHistory(Long userId, EntryType type) {
        return manualEntryRepository.findByUser_IdAndTypeOrderByEntryDateDesc(userId, type);
    }
}