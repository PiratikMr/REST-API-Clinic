package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.entities.Patient;
import lombok.Data;

@Data
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;

    public PatientResponse(Patient patient) {
        id = patient.getPatientId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        middleName = patient.getMiddleName();
        address = patient.getAddress();
    }
}
