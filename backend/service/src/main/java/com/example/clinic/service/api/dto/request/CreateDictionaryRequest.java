package com.example.clinic.service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание записи справочника")
public class CreateDictionaryRequest {
    @NotBlank
    @Schema(description = "Название элемента", example = "Хирург")
    private String name;
}
