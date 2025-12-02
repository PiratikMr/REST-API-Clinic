package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "procedures")
@Data
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "procedure_id")
    private Long procedureId;

    @Column(name = "name")
    private String name;
}
