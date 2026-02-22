package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.Patch.PatchPatientRequest;
import com.example.clinic.service.api.dto.response.MessageResponse;
import com.example.clinic.service.api.dto.response.PatientResponse;
import com.example.clinic.service.api.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.clinic.service.api.ApiPaths.PATIENTS;
import static com.example.clinic.service.core.security.SecurityConstants.HAS_ADMIN;
import static com.example.clinic.service.core.security.SecurityConstants.HAS_PATIENT;

@RestController
@RequestMapping(PATIENTS)
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{patientId}")
    @PreAuthorize(HAS_ADMIN + " or (" +
            HAS_PATIENT + " and @accessControl.isPatientOwner(#patientId, authentication))")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    @PatchMapping("/{patientId}")
    @PreAuthorize(HAS_ADMIN + " or " +
            HAS_PATIENT + " and @accessControl.isPatientOwner(#patientId, authentication)")
    public ResponseEntity<MessageResponse> updatePatient(
            @PathVariable Long patientId,
            @RequestBody PatchPatientRequest request
    ) {
        patientService.updatePatient(patientId, request);
        return ResponseEntity.ok(new MessageResponse("Данные успешно обновлены"));
    }

}
