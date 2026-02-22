package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Medication;
import com.example.clinic.service.entities.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findAllByOrderByNameAsc();

    Page<Medication> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
