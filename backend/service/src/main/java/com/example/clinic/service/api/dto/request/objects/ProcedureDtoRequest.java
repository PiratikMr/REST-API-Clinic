package com.example.clinic.service.api.dto.request.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Назначенная процедура")
public class ProcedureDtoRequest {
    @NotNull
    @Schema(description = "Идентификатор процедуры", example = "5")
    private Long procedureId;

    @Nullable
    @Schema(description = "Количество сеансов", example = "10")
    private Integer sessions;
}
