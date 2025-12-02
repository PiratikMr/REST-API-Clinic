package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByOrderByNameAsc();
}
