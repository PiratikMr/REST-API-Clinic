package com.example.clinic.service.api.dto.request.Patch;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.api.dto.request.objects.ScheduleDtoRequest;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос на частичное обновление данных доктора")
public class PatchDoctorRequest {
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
    @Schema(description = "Идентификатор специальности", example = "2")
    private Long specialtyId;

    @Nullable
    @Schema(description = "Рабочее расписание")
    private List<ScheduleDtoRequest> schedules;
}
