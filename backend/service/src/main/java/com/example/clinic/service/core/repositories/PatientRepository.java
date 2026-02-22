package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUserId(Long userId);
}
