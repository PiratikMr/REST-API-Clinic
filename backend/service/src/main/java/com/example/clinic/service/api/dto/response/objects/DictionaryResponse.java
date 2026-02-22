package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запись из справочника")
public class DictionaryResponse {
    @Schema(description = "Идентификатор записи", example = "10")
    private Long id;
    @Schema(description = "Значение (наименование)", example = "Аспирин")
    private String name;
}
