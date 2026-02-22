package com.example.clinic.service.entities;

import com.example.clinic.service.entities.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "slot_id", referencedColumnName = "slot_id")
    private DoctorSlot doctorSlot;

    @Column(name = "complaints")
    private String complaints;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;
}
