package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private java.time.LocalDate birthDate;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MedicalCard medicalCard;
}
