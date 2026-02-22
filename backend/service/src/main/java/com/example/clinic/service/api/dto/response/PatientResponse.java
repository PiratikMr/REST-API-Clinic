package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.clinic.service.entities.Patient;
import lombok.Data;

@Data
@Schema(description = "Информация о пациенте")
public class PatientResponse {
    @Schema(description = "Идентификатор пациента", example = "1")
    private Long id;
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;
    @Schema(description = "Адрес проживания", example = "г. Москва, ул. Пушкина, д. Колотушкина")
    private String address;
    @Schema(description = "Дата рождения", example = "1990-05-15")
    private java.time.LocalDate birthDate;
    @Schema(description = "Медицинская карта пациента")
    private com.example.clinic.service.entities.MedicalCard medicalCard;

    public PatientResponse(Patient patient) {
        id = patient.getPatientId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        middleName = patient.getMiddleName();
        address = patient.getAddress();
        birthDate = patient.getBirthDate();
        medicalCard = patient.getMedicalCard();
    }
}
