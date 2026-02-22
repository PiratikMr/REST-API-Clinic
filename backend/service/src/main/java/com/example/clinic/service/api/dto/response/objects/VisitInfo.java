package com.example.clinic.service.api.dto.response.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.entities.DoctorSlot;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о месте и времени визита")
public class VisitInfo {
    @Schema(description = "Время приема", type = "string", example = "2024-11-20T10:30")
    private LocalDateTime time;
    @Schema(description = "Кабинет", example = "101")
    private String room;
    @Schema(description = "Участок/район", example = "Центральный район")
    private String district;

    public VisitInfo(DoctorSlot slot) {
        this.time = slot.getStartTime();
        this.room = slot.getPlace().getRoom();
        this.district = slot.getPlace().getDistrict();
    }
}
