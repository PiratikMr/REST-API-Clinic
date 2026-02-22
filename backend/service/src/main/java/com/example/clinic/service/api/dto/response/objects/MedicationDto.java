package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Информация о назначенном медикаменте")
public class MedicationDto extends DictionaryResponse {
    @Schema(description = "Детали приема (дозировка, частота)", example = "По 1 таблетке 3 раза в день")
    private String details;
}
