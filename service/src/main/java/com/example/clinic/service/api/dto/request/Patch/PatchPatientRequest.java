package com.example.clinic.service.api.dto.request.Patch;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class PatchPatientRequest {
    @Nullable
    private String firstName;
    @Nullable
    private String middleName;
    @Nullable
    private String lastName;
    @Nullable
    private String address;
}
