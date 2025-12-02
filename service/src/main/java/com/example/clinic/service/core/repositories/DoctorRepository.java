package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserUserId(Long userId);

    Page<Doctor> findAll(Pageable pageable);
}
