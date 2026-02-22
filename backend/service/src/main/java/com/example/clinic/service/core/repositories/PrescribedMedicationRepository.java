package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Appointment;
import com.example.clinic.service.entities.Patient;
import com.example.clinic.service.entities.PrescribedMedication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescribedMedicationRepository extends JpaRepository<PrescribedMedication, Long> {
    List<PrescribedMedication> findAllByAppointment(Appointment appointment);
}
