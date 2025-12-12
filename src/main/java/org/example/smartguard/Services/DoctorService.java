package org.example.smartguard.Services;

import org.example.smartguard.Model.Doctor;
import org.example.smartguard.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor saveDoctor(Doctor doctor) {
        // Aici ar putea fi adaugata logica de validare
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        // Aici ar putea fi adaugata logica de verificare a permisiunilor
        doctorRepository.deleteById(id);
    }
}