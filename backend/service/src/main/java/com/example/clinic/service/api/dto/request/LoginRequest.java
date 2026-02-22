package com.example.clinic.service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на авторизацию пользователя")
public class LoginRequest {
    @NotBlank
    @Schema(description = "Логин пользователя", example = "ivanov_i")
    private String login;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "secretpassword")
    private String password;
}
