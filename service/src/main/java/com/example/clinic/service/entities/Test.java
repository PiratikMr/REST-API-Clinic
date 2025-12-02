package com.example.clinic.service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tests")
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(name = "name")
    private String name;
}
