package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByOrderByNameAsc();

    Page<Test> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
