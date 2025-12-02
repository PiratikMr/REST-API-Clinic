package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Appointment;
import com.example.clinic.service.entities.PrescribedTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescribedTestRepository extends JpaRepository<PrescribedTest, Long> {
    List<PrescribedTest> findAllByAppointment(Appointment appointment);
}
