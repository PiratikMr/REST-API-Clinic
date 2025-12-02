package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.Patch.PatchPatientRequest;
import com.example.clinic.service.api.dto.response.PatientResponse;
import com.example.clinic.service.api.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.clinic.service.api.ApiPaths.PATIENTS;

@RestController
@RequestMapping(PATIENTS)
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long patientId
    ) {
        try {
            return ResponseEntity.ok(patientService.getPatientById(patientId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long patientId,
            @RequestBody PatchPatientRequest request
    ) {
        try {
            patientService.updatePatient(patientId, request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
