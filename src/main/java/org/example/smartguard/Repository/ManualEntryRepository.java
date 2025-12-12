package org.example.smartguard.Repository;

import org.example.smartguard.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManualEntryRepository extends JpaRepository<ManualEntry, Long> {
    List<ManualEntry> findByUser_IdAndTypeOrderByEntryDateDesc(Long userId, EntryType type);
}