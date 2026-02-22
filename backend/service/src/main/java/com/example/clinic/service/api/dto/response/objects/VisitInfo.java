package com.example.clinic.service.api.dto.response.objects;

import com.example.clinic.service.entities.DoctorSlot;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitInfo {
    private LocalDateTime time;
    private String room;
    private String district;

    public VisitInfo(DoctorSlot slot) {
        this.time = slot.getStartTime();
        this.room = slot.getPlace().getRoom();
        this.district = slot.getPlace().getDistrict();
    }
}
