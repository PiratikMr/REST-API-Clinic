package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.entities.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о докторе")
public class DoctorResponse {
    @Schema(description = "Идентификатор доктора", example = "1")
    private Long id;
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;
    @Schema(description = "Специальность", example = "Хирург")
    private String specialty;

    public DoctorResponse(Doctor doctor) {
        id = doctor.getDoctorId();
        firstName = doctor.getFirstName();
        lastName = doctor.getLastName();
        middleName = doctor.getMiddleName();
        specialty = doctor.getSpecialty().getName();
    }
}
