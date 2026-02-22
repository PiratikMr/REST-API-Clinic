package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.api.dto.response.objects.VisitInfo;
import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentResponse {
    private Long id;
    private VisitInfo visitInfo;
    private String status;
    private String complaints;
    private String diagnosis;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getAppointmentId();
        this.visitInfo = new VisitInfo(appointment.getDoctorSlot());
        this.status = String.valueOf(appointment.getStatus());
        this.complaints = appointment.getComplaints();
        this.diagnosis = appointment.getDiagnosis();
    }
}
