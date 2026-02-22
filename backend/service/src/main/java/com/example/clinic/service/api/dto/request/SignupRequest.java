package com.example.clinic.service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию нового пациента")
public class SignupRequest {
    @NotBlank
    @Schema(description = "Логин", example = "ivanov_i")
    private String login;

    @NotBlank
    @Schema(description = "Пароль", example = "secretpassword")
    private String password;

    @NotBlank
    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Nullable
    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;
}
