package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class AppointmentForPatientResponse extends AppointmentResponse {
    private DoctorResponse doctor;

    public AppointmentForPatientResponse(Appointment appointment) {
        super(appointment);
        doctor = new DoctorResponse(appointment.getDoctorSlot().getDoctor());
    }
}
