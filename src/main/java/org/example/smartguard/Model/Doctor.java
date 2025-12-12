package org.example.smartguard.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date Autentificare
    private String username;
    private String passwordHash; // Parola hash-uită
    private String email;

    // Date Profesionale
    private String firstName;
    private String lastName;
    private String specialization; // Specializare medicală (ex: Cardiologie, Medicină Internă)
    private String medicalLicenseId; // Codul de licență/parafă

    // Relație cu Pacienții (O listă de Pacienți pe care îi monitorizează)
    // Dacă am avea o relație Many-to-Many sau Many-to-One
    // @OneToMany
    // private List<User> monitoredPatients;

    // --- Constructori, Getters și Setters ---

    public Doctor() {
    }

    // Constructor util pentru teste
    public Doctor(String username, String passwordHash, String firstName, String specialization) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.specialization = specialization;
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getMedicalLicenseId() { return medicalLicenseId; }
    public void setMedicalLicenseId(String medicalLicenseId) { this.medicalLicenseId = medicalLicenseId; }
}