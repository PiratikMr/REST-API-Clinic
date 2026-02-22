package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Appointment;
import com.example.clinic.service.entities.PrescribedProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescribedProcedureRepository extends JpaRepository<PrescribedProcedure, Long> {
    List<PrescribedProcedure> findAllByAppointment(Appointment appointment);
}
