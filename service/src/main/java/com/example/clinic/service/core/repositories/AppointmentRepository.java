package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Appointment;
import com.example.clinic.service.entities.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.status IN :statuses AND a.doctorSlot.doctor.doctorId = :doctorId")
    Page<Appointment> findAllByDoctorId(
            @Param("statuses") List<AppointmentStatus> statuses,
            @Param("doctorId") Long doctorId,
            Pageable pageable
    );

    @Query("SELECT a FROM Appointment a WHERE a.status IN :statuses AND a.patient.patientId = :patientId")
    Page<Appointment> findAllByPatientId(
            @Param("statuses") List<AppointmentStatus> statuses,
            @Param("patientId") Long patientId,
            Pageable pageable
    );
}
