package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    List<Specialty> findAllByOrderByNameAsc();

    Page<Specialty> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
