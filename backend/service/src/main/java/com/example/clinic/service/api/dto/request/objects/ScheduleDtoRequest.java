package com.example.clinic.service.api.dto.request.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Запись о рабочем расписании")
public class ScheduleDtoRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(description = "Время начала работы", type = "string", example = "2024-11-20T08:00")
    private LocalDateTime from;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(description = "Время окончания работы", type = "string", example = "2024-11-20T17:00")
    private LocalDateTime to;

    @NotBlank
    @Schema(description = "Кабинет", example = "101")
    private String room;

    @NotBlank
    @Schema(description = "Участок/район", example = "Центральный район")
    private String district;
}
