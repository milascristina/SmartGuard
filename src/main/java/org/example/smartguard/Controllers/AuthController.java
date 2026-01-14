package org.example.smartguard.Controllers;

import org.example.smartguard.Model.User;
import org.example.smartguard.Model.Doctor;
import org.example.smartguard.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Asigură comunicarea cu Frontend-ul
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginCredentials) {
        String username = loginCredentials.getUsername();
        String password = loginCredentials.getPassword();

        Map<String, Object> response = new HashMap<>();

        // --- LOGICA DE AUTENTIFICARE SOLICITATĂ ---
        if ("ana".equals(username) && "ana".equals(password)) {
            // Autentificare reușită pentru Ana (Pacient)
            response.put("id", 1L);
            response.put("firstName", "Ana");
            response.put("role", "PATIENT");
            response.put("weight", 57.0); // Datele tale de profil
            response.put("height", 1.67);
            response.put("goal", "Maraton");

            return ResponseEntity.ok(response);
        }

        // Logica existentă pentru simulare Doctor
        if (username != null && username.toLowerCase().contains("doctor")) {
            response.put("id", 999L);
            response.put("firstName", "Dr. Simulat");
            response.put("role", "DOCTOR");
            return ResponseEntity.ok(response);
        }

        // Dacă datele nu sunt "ana"/"ana", returnăm eroare de neautorizat
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Utilizator sau parolă incorectă.");
    }

    @PostMapping("/register/patient")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User savedUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = authService.registerDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}