package org.example.smartguard.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "manual_entry")
public class ManualEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private EntryType type; // ex: GENERIC, BLOOD_PRESSURE, MOOD

    private LocalDateTime entryDate; // Data introdusă de utilizator

    // --- Câmpuri noi pentru Fitness și Nutriție ---
    private Integer energyLevel; // Nivel energie 1-10 (Necesar pentru getEnergyLevel)

    @Column(length = 1000)
    private String notes; // Observații despre dieta cu fasole/cartofi sau maraton

    // --- Câmpuri specifice Medicale ---
    private Double numericValue; // Pentru greutate (ex: 57.0 kg)
    private Integer systolic;
    private Integer diastolic;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdOn;

    // --- Constructor ---
    public ManualEntry() {
    }

    // --- Getters and Setters ---

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public EntryType getType() { return type; }
    public void setType(EntryType type) { this.type = type; }

    public LocalDateTime getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }

    public Double getNumericValue() { return numericValue; }
    public void setNumericValue(Double numericValue) { this.numericValue = numericValue; }

    public Integer getSystolic() { return systolic; }
    public void setSystolic(Integer systolic) { this.systolic = systolic; }

    public Integer getDiastolic() { return diastolic; }
    public void setDiastolic(Integer diastolic) { this.diastolic = diastolic; }

    public LocalDateTime getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }
}