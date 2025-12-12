package org.example.smartguard.Controllers;

import org.example.smartguard.Model.Doctor;
import org.example.smartguard.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // CREATE: POST /api/doctors
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    // READ ALL: GET /api/doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // READ BY ID: GET /api/doctors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE: PUT /api/doctors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctorDetails) {
        return doctorService.getDoctorById(id)
                .map(existingDoctor -> {
                    // Actualizarea campurilor
                    existingDoctor.setUsername(doctorDetails.getUsername());
                    existingDoctor.setFirstName(doctorDetails.getFirstName());
                    existingDoctor.setLastName(doctorDetails.getLastName());
                    existingDoctor.setEmail(doctorDetails.getEmail());
                    existingDoctor.setSpecialization(doctorDetails.getSpecialization());
                    existingDoctor.setMedicalLicenseId(doctorDetails.getMedicalLicenseId());

                    Doctor updatedDoctor = doctorService.saveDoctor(existingDoctor);
                    return ResponseEntity.ok(updatedDoctor);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: DELETE /api/doctors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorService.getDoctorById(id).isPresent()) {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}