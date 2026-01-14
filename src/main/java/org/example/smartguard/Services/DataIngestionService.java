package org.example.smartguard.Services;

import org.example.smartguard.Model.*;
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

@Service
public class DataIngestionService {

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

    /**
     * MODIFICAT: S-a inversat ordinea citirii coloanelor pentru a se potrivi cu fisierul tau CSV.
     * Structura asteptata in CSV: Timestamp, Valoare, Tip (sau Timestamp, Valoare cu Tip default).
     */
    public void processSensorFile(MultipartFile file, Long userId) throws Exception {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizator negăsit: " + userId));

        if (file.isEmpty()) throw new IllegalArgumentException("Fișierul este gol.");

        List<SensorData> dataToSave = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isHeader) { isHeader = false; continue; }

                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                try {
                    // CORECTIE: Fisierul tau are timestamp pe prima coloana (parts[0])
                    Instant timestamp = Instant.parse(parts[0].trim());
                    Double value = Double.parseDouble(parts[1].trim());

                    // Verificam daca exista a treia coloana pentru tip, altfel presupunem HEART_RATE
                    DataType type = (parts.length >= 3)
                            ? DataType.valueOf(parts[2].trim().toUpperCase())
                            : DataType.HEART_RATE;

                    // VALIDARE MODUL 3 (Logica AI): Praguri pentru maraton
                    if (type == DataType.HEART_RATE) {
                        if (value > 200) System.out.println("ALERTA AI: Ritm cardiac critic detectat: " + value);
                        if (value < 40) System.out.println("ALERTA AI: Bradicardie detectata: " + value);
                    }

                    SensorData data = new SensorData();
                    data.setUser(user);
                    data.setTimestamp(timestamp);
                    data.setType(type);
                    data.setSensorValue(value); // Asigura-te ca in Model se numeste sensorValue
                    dataToSave.add(data);

                } catch (Exception e) {
                    System.err.println("Eroare la linia " + lineNumber + ": Coloana gresita sau format invalid.");
                }
            }
        }

        if (!dataToSave.isEmpty()) {
            sensorDataRepository.saveAll(dataToSave);
        }
    }

    /**
     * Salvează jurnalul manual (Energie, Note, Nutritie).
     * Ideal pentru monitorizarea dietei tale (cartofi, fasole).
     */
    public ManualEntry saveManualEntry(ManualEntry entry, Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizator negăsit."));

        entry.setUser(user);
        // Setam data curenta daca nu este trimisa de frontend
        if (entry.getEntryDate() == null) {
            entry.setEntryDate(LocalDateTime.now(ZoneOffset.UTC));
        }

        // VALIDARE MODUL 3: Verificare stare de spirit scazuta
        if (entry.getEnergyLevel() != null && entry.getEnergyLevel() < 3) {
            System.out.println("ALERTA AI: Nivel de energie foarte scazut raportat de pacient.");
        }

        return manualEntryRepository.save(entry);
    }

    public List<SensorData> getSensorDataHistory(Long userId, DataType type) {
        return sensorDataRepository.findByUser_IdAndTypeOrderByTimestampDesc(userId, type);
    }

    public List<ManualEntry> getManualEntryHistory(Long userId, EntryType type) {
        return manualEntryRepository.findByUser_IdAndTypeOrderByEntryDateDesc(userId, type);
    }
}