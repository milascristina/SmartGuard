package org.example.smartguard.Model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "sensor_data")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType type; // HEART_RATE, STEPS, etc.

    @Column(name = "sensor_value", nullable = false)
    private Double sensorValue; // Am redenumit din 'value' in 'sensorValue' pentru a se potrivi cu Service-ul

    @Column(nullable = false)
    private Instant timestamp; // Ora măsurătorii din smartwatch

    @CreationTimestamp
    @Column(name = "imported_at", updatable = false)
    private LocalDateTime importedAt;

    // --- Constructors ---
    public SensorData() {}

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DataType getType() { return type; }
    public void setType(DataType type) { this.type = type; }

    // Metodele de care avea nevoie DataIngestionService:
    public Double getSensorValue() { return sensorValue; }
    public void setSensorValue(Double sensorValue) { this.sensorValue = sensorValue; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public LocalDateTime getImportedAt() { return importedAt; }
    public void setImportedAt(LocalDateTime importedAt) { this.importedAt = importedAt; }
}