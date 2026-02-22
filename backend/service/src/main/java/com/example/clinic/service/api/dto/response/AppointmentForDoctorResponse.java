package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AppointmentForDoctorResponse extends AppointmentResponse {
    private PatientResponse patient;

    public AppointmentForDoctorResponse(Appointment appointment) {
        super(appointment);
        patient = new PatientResponse(appointment.getPatient());
    }
}
