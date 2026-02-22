package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "medical_cards")
@Data
public class MedicalCard {

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "chronic_diseases")
    private String chronicDiseases;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
}
