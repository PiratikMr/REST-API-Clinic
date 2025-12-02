package com.example.clinic.service.api.dto.request.Patch;

import com.example.clinic.service.api.dto.request.objects.ScheduleDtoRequest;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
public class PatchDoctorRequest {
    @Nullable
    private String firstName;
    @Nullable
    private String middleName;
    @Nullable
    private String lastName;

    @Nullable
    private Long specialtyId;

    @Nullable
    private List<ScheduleDtoRequest> schedules;
}
