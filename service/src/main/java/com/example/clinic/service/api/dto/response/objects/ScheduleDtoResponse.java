package com.example.clinic.service.api.dto.response.objects;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDtoResponse {
    private Long id;
    private String room;
    private String district;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
