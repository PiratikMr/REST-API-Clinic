package com.example.clinic.service.api.dto.request;

import com.example.clinic.service.api.dto.request.objects.ScheduleDtoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateDoctorRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    @NotNull
    private Long specialtyId;

    private List<ScheduleDtoRequest> schedules;
}
