package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.api.dto.response.objects.ScheduleDtoResponse;
import com.example.clinic.service.entities.Doctor;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class DoctorDetailResponse extends DoctorResponse {
    private List<ScheduleDtoResponse> schedules;

    public DoctorDetailResponse(Doctor doctor) {
        super(doctor);
    }
}
