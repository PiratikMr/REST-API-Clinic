package com.example.clinic.service.api.dto.response;

import com.example.clinic.service.api.dto.response.objects.MedicationDto;
import com.example.clinic.service.api.dto.response.objects.ProcedureDto;
import com.example.clinic.service.api.dto.response.objects.TestDto;
import com.example.clinic.service.entities.Appointment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AppointmentDetailResponse extends AppointmentResponse {
    private DoctorResponse doctor;
    private PatientResponse patient;

    private List<MedicationDto> medications;
    private List<ProcedureDto> procedures;
    private List<TestDto> tests;

    public AppointmentDetailResponse(Appointment appointment) {
        super(appointment);

        doctor = new DoctorResponse(appointment.getDoctorSlot().getDoctor());
        patient = new PatientResponse(appointment.getPatient());
    }
}
