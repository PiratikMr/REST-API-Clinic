package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prescribed_procedures")
@Data
public class PrescribedProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescribed_proc_id")
    private Long prescribedProcedureId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "procedure_id", referencedColumnName = "procedure_id")
    private Procedure procedure;

    @Column(name = "session_count")
    private Integer sessionCount;
}
