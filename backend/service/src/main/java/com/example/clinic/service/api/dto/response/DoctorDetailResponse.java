package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.api.dto.response.objects.ScheduleDtoResponse;
import com.example.clinic.service.entities.Doctor;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Детальная информация о докторе (с расписанием)")
public class DoctorDetailResponse extends DoctorResponse {
    @Schema(description = "Рабочее расписание доктора")
    private List<ScheduleDtoResponse> schedules;

    public DoctorDetailResponse(Doctor doctor) {
        super(doctor);
    }
}
