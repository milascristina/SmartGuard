package org.example.smartguard.Controllers;

import org.example.smartguard.Services.DataIngestionService;
import org.example.smartguard.Model.SensorData;
import org.example.smartguard.Model.ManualEntry;
import org.example.smartguard.Model.DataType;
import org.example.smartguard.Model.EntryType;
import org.example.smartguard.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataIngestionService ingestionService;

    // Injectare prin Constructor
    public DataController(DataIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    // --- LOGICA MOCĂITĂ PENTRU ID (FĂRĂ SECURITATE) ---
    private Long getCurrentUserId() {
        // Returnează un ID fix al unui utilizator existent (trebuie să existe 1L în baza de date)
        return 1L;
    }
    // --- SFÂRȘITUL LOGICII MOCĂITE ---

    // --- 1. Încărcare Date Senzori ---
    @PostMapping("/upload/sensor")
    public ResponseEntity<String> uploadSensorData(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();

            ingestionService.processSensorFile(file, userId);
            return ResponseEntity.ok("Datele de la senzori au fost încărcate și procesate cu succes.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare de validare: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la procesarea fișierului: " + e.getMessage());
        }
    }

    // --- 2. Intrare Manuală ---
    @PostMapping("/manual/entry")
    public ResponseEntity<ManualEntry> createManualEntry(@RequestBody ManualEntry entry) {
        try {
            Long userId = getCurrentUserId();
            ManualEntry savedEntry = ingestionService.saveManualEntry(entry, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // --- 3. Istoric Date Senzori ---
    @GetMapping("/history/sensor/{type}")
    public ResponseEntity<List<SensorData>> getSensorHistory(@PathVariable DataType type) {
        Long userId = getCurrentUserId();
        List<SensorData> history = ingestionService.getSensorDataHistory(userId, type);
        return ResponseEntity.ok(history);
    }

    // --- 4. Istoric Intrări Manuale ---
    @GetMapping("/history/manual/{type}")
    public ResponseEntity<List<ManualEntry>> getManualEntryHistory(@PathVariable EntryType type) {
        Long userId = getCurrentUserId();
        List<ManualEntry> history = ingestionService.getManualEntryHistory(userId, type);
        return ResponseEntity.ok(history);
    }
}