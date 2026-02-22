package com.example.clinic.service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание записи к врачу")
public class CreateAppointmentRequest {
    @NotNull
    @Schema(description = "Идентификатор пациента", example = "1")
    private Long patientId;

    @NotNull
    @Schema(description = "Идентификатор слота (времени приема)", example = "42")
    private Long doctorSlotId;
}
