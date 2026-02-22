package com.example.clinic.service.api.dto.request;

import com.example.clinic.service.api.dto.request.objects.ScheduleDtoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос на создание нового доктора")
public class CreateDoctorRequest {
    @NotBlank
    @Schema(
            description = "Логин для входа в систему",
            example = "doctor_shon"
    )
    private String login;

    @NotBlank
    @Schema(
            description = "Пароль пользователя",
            example = "secure_password_1234"
    )
    private String password;

    @NotBlank
    @Schema(
            description = "Имя доктора",
            example = "Иван"
    )
    private String firstName;

    @NotBlank
    @Schema(
            description = "Фамилия доктора",
            example = "Иванов"
    )
    private String lastName;

    @Nullable
    @Schema(
            description = "Отчество доктора",
            example = "Иванович"
    )
    private String middleName;

    @NotNull
    @Schema(
            description = "ID специальности",
            example = "1"
    )
    private Long specialtyId;

    @Nullable
    @Schema(
            description = "График работы"
    )
    private List<ScheduleDtoRequest> schedules;
}
