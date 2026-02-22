package com.example.clinic.service.api.dto.request.Patch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchDictionaryRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
