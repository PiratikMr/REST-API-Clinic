package com.example.clinic.service.api.dto.request.objects;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestDtoRequest {
    @NotNull
    private Long id;

    @Nullable
    private String results;
}
