package com.example.clinic.service.api.dto.request.Patch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление записи справочника")
public class PatchDictionaryRequest {
    @NotNull
    @Schema(description = "Идентификатор записи", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Новое название элемента", example = "Хирург-эндокринолог")
    private String name;
}
