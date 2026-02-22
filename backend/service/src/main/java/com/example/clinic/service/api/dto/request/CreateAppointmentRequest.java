package com.example.clinic.service.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAppointmentRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorSlotId;
}
