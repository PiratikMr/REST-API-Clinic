package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.CloseAppointmentRequest;
import com.example.clinic.service.api.dto.request.CreateAppointmentRequest;
import com.example.clinic.service.api.dto.response.AppointmentDetailResponse;
import com.example.clinic.service.api.dto.response.AppointmentForDoctorResponse;
import com.example.clinic.service.api.dto.response.AppointmentForPatientResponse;
import com.example.clinic.service.api.dto.response.PagedResponse;
import com.example.clinic.service.api.services.AppointmentService;
import com.example.clinic.service.entities.enums.AppointmentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.clinic.service.api.ApiPaths.*;

@RestController
@RequestMapping(APPOINTMENTS)
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping(DOCTORS + "/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<PagedResponse<AppointmentForDoctorResponse>> getAppointmentsForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "ACTIVE") List<AppointmentStatus> statuses
    ) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForDoctor(doctorId, statuses, page));
    }

    @GetMapping(PATIENTS + "/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<PagedResponse<AppointmentForPatientResponse>> getAppointmentsForPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "ACTIVE") List<AppointmentStatus> statuses
    ) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForPatient(patientId, statuses, page));
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        appointmentService.createAppointment(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDetailResponse> getDetailAppointment(
            @PathVariable Long appointmentId
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }


    @PostMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId
    ) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{appointmentId}/close")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> closeAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody CloseAppointmentRequest request
    ) {
        appointmentService.closeAppointment(appointmentId, request);
        return ResponseEntity.ok().build();
    }
}
