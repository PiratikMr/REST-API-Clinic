package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Информация о записи на прием (для доктора)")
public class AppointmentForDoctorResponse extends AppointmentResponse {
    @Schema(description = "Пациент")
    private PatientResponse patient;

    public AppointmentForDoctorResponse(Appointment appointment) {
        super(appointment);
        patient = new PatientResponse(appointment.getPatient());
    }
}
