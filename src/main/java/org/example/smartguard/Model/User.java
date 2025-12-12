package org.example.smartguard.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String passwordHash; // Parola ar trebui stocată mereu hash-uită!
    private String email;

    // Date Personale
    private String firstName;
    private String lastName;
    private Integer age;
    private Double weightKg;
    private Double heightCm;

    // Date de Sănătate (Sumarizate/Agregate)
    private Double avgRestingHeartRate; // Ritm cardiac mediu de repaus
    private Double lastSpO2; // Ultima saturație a oxigenului
    private Integer dailyStepsGoal; // Obiectivul zilnic de pași
    // Relație (dacă ar fi necesară legătura cu datele brute de la ceas)
    // De exemplu: @OneToMany(mappedBy = "user")
    // private List<SmartwatchData> healthRecords;

    // --- Constructori, Getters și Setters ---

    public User() {
    }

    // Constructor util pentru teste
    public User(String username, String passwordHash, String firstName) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
    }

    // GETTERS & SETTERS (Se adaugă automat cu Lombok, dar le includem manual pentru claritate)
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
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }
    public Double getAvgRestingHeartRate() { return avgRestingHeartRate; }
    public void setAvgRestingHeartRate(Double avgRestingHeartRate) { this.avgRestingHeartRate = avgRestingHeartRate; }
    public Double getLastSpO2() { return lastSpO2; }
    public void setLastSpO2(Double lastSpO2) { this.lastSpO2 = lastSpO2; }
    public Integer getDailyStepsGoal() { return dailyStepsGoal; }
    public void setDailyStepsGoal(Integer dailyStepsGoal) { this.dailyStepsGoal = dailyStepsGoal; }
}