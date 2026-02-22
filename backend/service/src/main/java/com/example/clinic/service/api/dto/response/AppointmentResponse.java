package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.api.dto.response.objects.VisitInfo;
import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о записи на прием")
public class AppointmentResponse {
    @Schema(description = "Идентификатор записи", example = "12")
    private Long id;
    @Schema(description = "Информация о визите")
    private VisitInfo visitInfo;
    @Schema(description = "Статус приема", example = "ACTIVE")
    private String status;
    @Schema(description = "Жалобы пациента", example = "Головные боли, слабость")
    private String complaints;
    @Schema(description = "Диагноз", example = "ОРВИ")
    private String diagnosis;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getAppointmentId();
        this.visitInfo = new VisitInfo(appointment.getDoctorSlot());
        this.status = String.valueOf(appointment.getStatus());
        this.complaints = appointment.getComplaints();
        this.diagnosis = appointment.getDiagnosis();
    }
}
