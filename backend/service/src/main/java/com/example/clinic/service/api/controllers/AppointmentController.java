package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.CloseAppointmentRequest;
import com.example.clinic.service.api.dto.request.CreateAppointmentRequest;
import com.example.clinic.service.api.dto.response.*;
import com.example.clinic.service.api.services.AppointmentService;
import com.example.clinic.service.entities.enums.AppointmentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.clinic.service.api.ApiPaths.*;
import static com.example.clinic.service.core.security.SecurityConstants.*;

@RestController
@RequestMapping(APPOINTMENTS)
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping(DOCTORS + "/{doctorId}")
    @PreAuthorize(HAS_ADMIN + " or (" +
            HAS_DOCTOR + " and @accessControl.isDoctorOwner(#doctorId, authentication))")
    public ResponseEntity<PagedResponse<AppointmentForDoctorResponse>> getAppointmentsForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "ACTIVE") List<AppointmentStatus> statuses
    ) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForDoctor(doctorId, statuses, page));
    }

    @GetMapping(PATIENTS + "/{patientId}")
    @PreAuthorize(HAS_ADMIN + " or (" +
            HAS_PATIENT + " and @accessControl.isPatientOwner(#patientId, authentication))")
    public ResponseEntity<PagedResponse<AppointmentForPatientResponse>> getAppointmentsForPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "ACTIVE") List<AppointmentStatus> statuses
    ) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForPatient(patientId, statuses, page));
    }

    @PostMapping
    @PreAuthorize(HAS_PATIENT + " and @accessControl.isPatientOwner(#request.patientId, authentication)")
    public ResponseEntity<MessageResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        appointmentService.createAppointment(request);
        return ResponseEntity.ok(new MessageResponse("Запись успешно создана"));
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize(HAS_ADMIN + " or " +
            "(@accessControl.isAppointmentOwner(#appointmentId, authentication)) or " +
            "(@accessControl.isAppointmentDoctor(#appointmentId, authentication))")
    public ResponseEntity<AppointmentDetailResponse> getDetailAppointment(
            @PathVariable Long appointmentId
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }


    @PostMapping("/{appointmentId}/cancel")
    @PreAuthorize(HAS_ADMIN + " or (" +
            HAS_PATIENT + " and @accessControl.isAppointmentOwner(#appointmentId, authentication))")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId
    ) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{appointmentId}/close")
    @PreAuthorize(HAS_ADMIN + " or (" +
            HAS_DOCTOR + " and @accessControl.isAppointmentDoctor(#appointmentId, authentication))")
    public ResponseEntity<?> closeAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody CloseAppointmentRequest request
    ) {
        appointmentService.closeAppointment(appointmentId, request);
        return ResponseEntity.ok().build();
    }
}
