package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Запись рабочего расписания доктора")
public class ScheduleDtoResponse {
    @Schema(description = "Идентификатор слота", example = "42")
    private Long id;
    @Schema(description = "Кабинет", example = "101")
    private String room;
    @Schema(description = "Участок/район", example = "Центральный район")
    private String district;
    @Schema(description = "Время начала приема", type = "string", example = "2024-11-20T08:00")
    private LocalDateTime startTime;
    @Schema(description = "Время окончания приема", type = "string", example = "2024-11-20T17:00")
    private LocalDateTime endTime;
}
