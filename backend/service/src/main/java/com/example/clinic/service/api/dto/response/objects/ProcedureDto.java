package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Информация о назначенной процедуре")
public class ProcedureDto extends DictionaryResponse {
    @Schema(description = "Количество сеансов", example = "10")
    private Integer sessions;
}
