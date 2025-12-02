package com.example.clinic.service.api.dto.request;

import com.example.clinic.service.api.dto.request.objects.MedicationDtoRequest;
import com.example.clinic.service.api.dto.request.objects.ProcedureDtoRequest;
import com.example.clinic.service.api.dto.request.objects.TestDtoRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CloseAppointmentRequest {
    @Nullable
    private List<MedicationDtoRequest> medications;
    @Nullable
    private List<ProcedureDtoRequest> procedures;
    @Nullable
    private List<TestDtoRequest> tests;

    @NotBlank
    private String complaints;

    @NotBlank
    private String diagnosis;
}
