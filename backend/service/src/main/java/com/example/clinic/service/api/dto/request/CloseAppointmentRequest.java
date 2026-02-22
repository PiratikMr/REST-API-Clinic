package com.example.clinic.service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.api.dto.request.objects.MedicationDtoRequest;
import com.example.clinic.service.api.dto.request.objects.ProcedureDtoRequest;
import com.example.clinic.service.api.dto.request.objects.TestDtoRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос на закрытие (завершение) приема врачом")
public class CloseAppointmentRequest {
    @Nullable
    @Schema(description = "Назначенные медикаменты")
    private List<MedicationDtoRequest> medications;
    @Nullable
    @Schema(description = "Назначенные процедуры")
    private List<ProcedureDtoRequest> procedures;
    @Nullable
    @Schema(description = "Назначенные анализы")
    private List<TestDtoRequest> tests;

    @NotBlank
    @Schema(description = "Жалобы пациента", example = "Головные боли, слабость")
    private String complaints;

    @NotBlank
    @Schema(description = "Диагноз", example = "ОРВИ")
    private String diagnosis;
}
