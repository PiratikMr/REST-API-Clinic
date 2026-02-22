package com.example.clinic.service.core.repositories;

import com.example.clinic.service.entities.Doctor;
import com.example.clinic.service.entities.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot,Long> {
    DoctorSlot findByDoctorDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime);

    @Query("SELECT s FROM DoctorSlot s " +
            "WHERE s.doctor = :doctor " +
            "AND s.startTime BETWEEN CURRENT_TIMESTAMP AND :endDate " +
            "AND s.slotId NOT IN (" +
            "    SELECT a.doctorSlot.slotId " +
            "    FROM Appointment a " +
            "    WHERE a.status = com.example.clinic.service.entities.enums.AppointmentStatus.ACTIVE " +
            "       OR a.status = com.example.clinic.service.entities.enums.AppointmentStatus.CLOSED" +
            ")")
    List<DoctorSlot> findAvailableDoctorSlotsByDoctor(
            @Param("doctor") Doctor doctor,
            @Param("endDate") LocalDateTime endDate
    );
}
