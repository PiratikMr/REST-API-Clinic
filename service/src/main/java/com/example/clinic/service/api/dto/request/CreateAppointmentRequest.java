package com.example.clinic.service.api.dto.request;

import lombok.Data;

@Data
public class CreateAppointmentRequest {
    private Long patientId;
    private Long doctorSlotId;
}
