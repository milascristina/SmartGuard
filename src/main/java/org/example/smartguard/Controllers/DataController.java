package org.example.smartguard.Controllers;

import org.example.smartguard.Services.DataIngestionService;
import org.example.smartguard.Model.SensorData;
import org.example.smartguard.Model.ManualEntry;
import org.example.smartguard.Model.DataType;
import org.example.smartguard.Model.EntryType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://localhost:5173") // Permite apelurile de la frontend-ul Vue
public class DataController {

    private final DataIngestionService ingestionService;

    public DataController(DataIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    // --- 1. Modulul 1: Încărcare Date Senzori (CSV) ---
    @PostMapping("/upload/sensor")
    public ResponseEntity<String> uploadSensorData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) { // Preluăm ID-ul trimis din frontend
        try {
            ingestionService.processSensorFile(file, userId);
            return ResponseEntity.ok("Datele senzorilor au fost procesate cu succes.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Eroare la procesarea fișierului: " + e.getMessage());
        }
    }

    // --- 2. Modulul 1: Intrare Manuală (Jurnal) ---
    @PostMapping("/manual/entry")
    public ResponseEntity<ManualEntry> createManualEntry(
            @RequestBody ManualEntry entry,
            @RequestParam("userId") Long userId) {
        try {
            ManualEntry savedEntry = ingestionService.saveManualEntry(entry, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // --- 3. Modulul 2: Vizualizare Istoric (Pentru Grafice) ---
    @GetMapping("/history/sensor/{type}")
    public ResponseEntity<List<SensorData>> getSensorHistory(
            @PathVariable DataType type,
            @RequestParam("userId") Long userId) {
        List<SensorData> history = ingestionService.getSensorDataHistory(userId, type);
        return ResponseEntity.ok(history);
    }

    // --- 4. Modulul 2: Istoric Jurnal Manual ---
    @GetMapping("/history/manual/{type}")
    public ResponseEntity<List<ManualEntry>> getManualEntryHistory(
            @PathVariable EntryType type,
            @RequestParam("userId") Long userId) {
        List<ManualEntry> history = ingestionService.getManualEntryHistory(userId, type);
        return ResponseEntity.ok(history);
    }
}