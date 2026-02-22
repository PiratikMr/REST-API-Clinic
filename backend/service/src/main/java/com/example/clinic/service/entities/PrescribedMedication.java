package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prescribed_medications")
@Data
public class PrescribedMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescribed_med_id")
    private Long prescribedMedicationId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "medication_id", referencedColumnName = "medication_id")
    private Medication medication;

    @Column(name = "details")
    private String details;
}
