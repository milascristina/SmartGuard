package org.example.smartguard.Repository;

import org.example.smartguard.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    // Pentru a prelua datele utilizatorului pentru o anumită categorie (ex: HEART_RATE)
    List<SensorData> findByUser_IdAndTypeOrderByTimestampDesc(Long userId, DataType type);
}