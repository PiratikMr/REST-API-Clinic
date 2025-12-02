package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.CloseAppointmentRequest;
import com.example.clinic.service.api.dto.request.CreateAppointmentRequest;
import com.example.clinic.service.api.dto.request.objects.MedicationDtoRequest;
import com.example.clinic.service.api.dto.request.objects.ProcedureDtoRequest;
import com.example.clinic.service.api.dto.request.objects.TestDtoRequest;
import com.example.clinic.service.api.dto.response.*;
import com.example.clinic.service.api.dto.response.objects.MedicationDto;
import com.example.clinic.service.api.dto.response.objects.ProcedureDto;
import com.example.clinic.service.api.dto.response.objects.TestDto;
import com.example.clinic.service.core.repositories.*;
import com.example.clinic.service.entities.*;
import com.example.clinic.service.entities.enums.AppointmentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.clinic.service.api.services.ServiceConfig.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorSlotRepository doctorSlotRepository;
    private final PrescribedProcedureRepository prescribedProcedureRepository;
    private final PrescribedMedicationRepository prescribedMedicationRepository;
    private final PrescribedTestRepository prescribedTestRepository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final TestRepository testRepository;


    public PagedResponse<AppointmentForDoctorResponse> getAllAppointmentsForDoctor(
            Long doctorId,
            List<AppointmentStatus> statuses,
            int page
    ) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("doctorSlot.startTime").ascending());
        Page<Appointment> appointments = appointmentRepository.findAllByDoctorId(statuses, doctorId, pageable);
        return new PagedResponse<>(appointments.map(AppointmentForDoctorResponse::new));
    }

    public PagedResponse<AppointmentForPatientResponse> getAllAppointmentsForPatient(
            Long patientId,
            List<AppointmentStatus> statuses,
            int page
    ) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("doctorSlot.startTime").ascending());
        Page<Appointment> appointments = appointmentRepository.findAllByPatientId(statuses, patientId, pageable);
        return new PagedResponse<>(appointments.map(AppointmentForPatientResponse::new));
    }


    public void createAppointment(CreateAppointmentRequest request) {
        Appointment appointment = new Appointment();

        appointment.setDoctorSlot(doctorSlotRepository.findById(request.getDoctorSlotId()).orElseThrow());
        appointment.setPatient(patientRepository.findById(request.getPatientId()).orElseThrow());
        appointment.setStatus(AppointmentStatus.ACTIVE);

        appointmentRepository.save(appointment);
    }

    public AppointmentDetailResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();

        List<PrescribedMedication> medications = prescribedMedicationRepository.findAllByAppointment(appointment);
        List<PrescribedProcedure> procedure = prescribedProcedureRepository.findAllByAppointment(appointment);
        List<PrescribedTest> tests = prescribedTestRepository.findAllByAppointment(appointment);

        AppointmentDetailResponse dto = new AppointmentDetailResponse(appointment);
        dto.setMedications(medications.stream().map(this::convertToMedicationDto).toList());
        dto.setProcedures(procedure.stream().map(this::convertToProcedureDto).toList());
        dto.setTests(tests.stream().map(this::convertToTestDto).toList());

        return dto;
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void closeAppointment(Long appointmentId, CloseAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();

        if (request.getMedications() != null) {
            for (MedicationDtoRequest dto : request.getMedications()) {
                PrescribedMedication med = new PrescribedMedication();

                med.setAppointment(appointment);
                med.setMedication(medicationRepository.findById(dto.getMedicationId()).orElseThrow());
                med.setDetails(dto.getDetails());

                prescribedMedicationRepository.save(med);
            }
        }

        if (request.getProcedures() != null) {
            for (ProcedureDtoRequest dto : request.getProcedures()) {
                PrescribedProcedure proc = new PrescribedProcedure();

                proc.setAppointment(appointment);
                proc.setProcedure(procedureRepository.findById(dto.getId()).orElseThrow());
                proc.setSessionCount(dto.getSessions());

                prescribedProcedureRepository.save(proc);
            }
        }

        if (request.getTests() != null) {
            for (TestDtoRequest dto : request.getTests()) {
                PrescribedTest test = new PrescribedTest();

                test.setAppointment(appointment);
                test.setTest(testRepository.findById(dto.getId()).orElseThrow());
                test.setResult(dto.getResults());

                prescribedTestRepository.save(test);
            }
        }

        appointment.setStatus(AppointmentStatus.CLOSED);
        appointment.setComplaints(request.getComplaints());
        appointment.setDiagnosis(request.getDiagnosis());

        appointmentRepository.save(appointment);
    }


    private MedicationDto convertToMedicationDto(PrescribedMedication medication) {
        MedicationDto dto = new MedicationDto();
        dto.setId(medication.getMedication().getMedicationId());
        dto.setName(medication.getMedication().getName());
        dto.setDetails(medication.getDetails());

        return dto;
    }

    private ProcedureDto convertToProcedureDto(PrescribedProcedure procedure) {
        ProcedureDto dto = new ProcedureDto();
        dto.setId(procedure.getProcedure().getProcedureId());
        dto.setName(procedure.getProcedure().getName());
        dto.setSessions(procedure.getSessionCount());

        return dto;
    }

    private TestDto convertToTestDto(PrescribedTest test) {
        TestDto dto = new TestDto();
        dto.setId(test.getTest().getTestId());
        dto.setName(test.getTest().getName());
        dto.setResult(test.getResult());

        return dto;
    }
}
