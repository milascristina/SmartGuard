package org.example.smartguard.Services;

import org.example.smartguard.Model.User;
import org.example.smartguard.Model.Doctor;
import org.example.smartguard.Repository.DoctorRepository;
import org.example.smartguard.Repository.UserRepository;
// Folosim PasswordEncoder, nu BCryptPasswordEncoder direct
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    // Declarațiile trebuie să fie final pentru injecția prin constructor
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository; // Injectat corect
    private final PasswordEncoder passwordEncoder;  // Injectat corect

    // Injectarea prin constructor (ordinea parametrilor nu este crucială, dar coerența ajută)
    public AuthService(UserRepository userRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        // Asigurăm că toate câmpurile sunt inițializate corect
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository; // Inițializat în locul corect
        this.passwordEncoder = passwordEncoder;
    }

    // --- Autentificare Pacient ---
    public Optional<User> authenticateUser(String username, String password) {
        // Linia 23 (unde era NPE-ul) este acum sigură, deoarece userRepository nu va fi null
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return userOptional;
            }
        }
        return Optional.empty();
    }

    // --- Autentificare Doctor ---
    public Optional<Doctor> authenticateDoctor(String username, String password) {
        Optional<Doctor> doctorOptional = doctorRepository.findByUsername(username);

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            // LOGICA TA ACTUALĂ DE TESTARE
            if (doctor.getPasswordHash().equals(password)) {
                // LOGICA SECURIZATĂ RECOMANDATĂ:
                // if (passwordEncoder.matches(password, doctor.getPasswordHash())) {
                return doctorOptional;
            }
        }
        return Optional.empty();
    }

    // --- Inregistrare Pacient (Folosește hashing real) ---
    public User registerUser(User user) {
        // Verificare existență (ar trebui adăugată aici)
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Numele de utilizator este deja folosit.");
        }
        // HASHING OBLIGATORIU
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    // --- Inregistrare Doctor (Folosește hashing real) ---
    public Doctor registerDoctor(Doctor doctor) {
        // Verificare existență (ar trebui adăugată aici)
        if (doctorRepository.findByUsername(doctor.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Numele de utilizator este deja folosit.");
        }
        // HASHING OBLIGATORIU
        doctor.setPasswordHash(passwordEncoder.encode(doctor.getPasswordHash()));
        return doctorRepository.save(doctor);
    }
}