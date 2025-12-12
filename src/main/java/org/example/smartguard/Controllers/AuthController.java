package org.example.smartguard.Controllers;

import org.example.smartguard.Model.User;
import org.example.smartguard.Model.Doctor;
import org.example.smartguard.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Clasa AuthResponse eliminată

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // --- 1. Autentificare Unificată (Folosind Map pentru Răspuns) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginCredentials) {
        String username = loginCredentials.getUsername();
        String password = loginCredentials.getPasswordHash();

        // Încearcă autentificarea ca Pacient
        Optional<User> userOptional = authService.authenticateUser(username, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Construim răspunsul securizat cu Map
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("firstName", user.getFirstName());
            response.put("role", "PATIENT");
            return ResponseEntity.ok(response);
        }

        // Încearcă autentificarea ca Doctor
        Optional<Doctor> doctorOptional = authService.authenticateDoctor(username, password);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            // Construim răspunsul securizat cu Map
            Map<String, Object> response = new HashMap<>();
            response.put("id", doctor.getId());
            response.put("firstName", doctor.getFirstName());
            response.put("role", "DOCTOR");
            return ResponseEntity.ok(response);
        }

        // Eșuat
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nume utilizator sau parolă incorectă.");
    }

    // --- 2. Înregistrare Pacient ---
    // Păstrăm ResponseEntity<User> - ATENȚIE: Aceasta tot expune passwordHash, dar respectă cerința de a nu folosi o clasă suplimentară.
    @PostMapping("/register/patient")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User savedUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // --- 3. Înregistrare Doctor ---
    @PostMapping("/register/doctor")
    public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = authService.registerDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}