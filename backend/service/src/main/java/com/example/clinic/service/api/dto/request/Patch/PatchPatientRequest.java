package com.example.clinic.service.api.dto.request.Patch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@Schema(description = "Запрос на частичное обновление данных пациента")
public class PatchPatientRequest {
    @Nullable
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @Nullable
    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;
    @Nullable
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
    @Nullable
    @Schema(description = "Адрес проживания", example = "г. Москва, ул. Пушкина, д. Колотушкина")
    private String address;
    @Nullable
    @Schema(description = "Дата рождения", example = "1990-05-15")
    private java.time.LocalDate birthDate;
}
