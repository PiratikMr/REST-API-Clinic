package com.example.clinic.service.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDictionaryRequest {
    @NotBlank
    private String name;
}
