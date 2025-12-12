package org.example.smartguard.Repository;

import org.example.smartguard.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Functii CRUD oferite automat de JpaRepository:
    // User save(User user);                      // C (Create/Update)
    // Optional<User> findById(Long id);         // R (Read by ID)
    // List<User> findAll();                      // R (Read All)
    // void deleteById(Long id);                 // D (Delete)

    // Functie personalizata pentru logare (gasirea utilizatorului dupa username)
    Optional<User> findByUsername(String username);

    // Functie pentru a gasi toti pacientii monitorizati de un anumit medic
    //List<User> findAllByCurrentDoctorId(Long doctorId);
}