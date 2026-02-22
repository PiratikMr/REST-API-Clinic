package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Информация о назначенном анализе")
public class TestDto extends DictionaryResponse {
    @Schema(description = "Результаты анализа", example = "В норме")
    private String result;
}
