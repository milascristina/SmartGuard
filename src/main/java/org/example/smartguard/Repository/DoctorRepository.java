package org.example.smartguard.Repository;

import org.example.smartguard.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Functii CRUD oferite automat de JpaRepository:
    // Doctor save(Doctor doctor);
    // Optional<Doctor> findById(Long id);
    // List<Doctor> findAll();
    // void deleteById(Long id);

    // Functie personalizata pentru logare
    Optional<Doctor> findByUsername(String username);
}
