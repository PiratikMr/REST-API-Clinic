package com.example.clinic.service.api.dto.response.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicationDto extends DictionaryResponse {
    private String details;
}
