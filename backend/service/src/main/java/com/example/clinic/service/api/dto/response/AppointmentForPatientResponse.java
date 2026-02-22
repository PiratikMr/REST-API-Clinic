package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Информация о записи на прием (для пациента)")
public class AppointmentForPatientResponse extends AppointmentResponse {
    @Schema(description = "Назначенный доктор")
    private DoctorResponse doctor;

    public AppointmentForPatientResponse(Appointment appointment) {
        super(appointment);
        doctor = new DoctorResponse(appointment.getDoctorSlot().getDoctor());
    }
}
