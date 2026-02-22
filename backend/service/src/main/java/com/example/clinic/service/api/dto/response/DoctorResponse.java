package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.entities.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String specialty;

    public DoctorResponse(Doctor doctor) {
        id = doctor.getDoctorId();
        firstName = doctor.getFirstName();
        lastName = doctor.getLastName();
        middleName = doctor.getMiddleName();
        specialty = doctor.getSpecialty().getName();
    }
}
