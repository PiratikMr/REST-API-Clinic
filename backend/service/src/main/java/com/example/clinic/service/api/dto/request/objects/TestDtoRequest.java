package com.example.clinic.service.api.dto.request.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Назначенный анализ")
public class TestDtoRequest {
    @NotNull
    @Schema(description = "Идентификатор анализа", example = "2")
    private Long testId;

    @Nullable
    @Schema(description = "Результаты (если уже известны)", example = "В норме")
    private String results;
}
