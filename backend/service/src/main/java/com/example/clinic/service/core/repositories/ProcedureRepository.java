package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Procedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<Procedure> findAllByOrderByNameAsc();

    Page<Procedure> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
