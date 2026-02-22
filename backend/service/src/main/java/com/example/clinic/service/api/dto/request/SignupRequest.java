package com.example.clinic.service.api.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;


    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Nullable
    private String middleName;

    @NotBlank
    private String address;
}
