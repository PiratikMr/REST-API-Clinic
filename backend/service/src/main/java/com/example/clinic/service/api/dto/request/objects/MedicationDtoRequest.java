package com.example.clinic.service.api.dto.request.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Назначенный медикамент")
public class MedicationDtoRequest {
    @NotNull
    @Schema(description = "Идентификатор медикамента", example = "10")
    private Long medicationId;

    @Nullable
    @Schema(description = "Детали приема (дозировка, частота)", example = "По 1 таблетке 3 раза в день")
    private String details;
}
