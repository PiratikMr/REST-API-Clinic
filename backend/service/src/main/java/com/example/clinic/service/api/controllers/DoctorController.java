package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.CreateDoctorRequest;
import com.example.clinic.service.api.dto.request.Patch.PatchDoctorRequest;
import com.example.clinic.service.api.dto.response.DoctorDetailResponse;
import com.example.clinic.service.api.dto.response.DoctorResponse;
import com.example.clinic.service.api.dto.response.MessageResponse;
import com.example.clinic.service.api.dto.response.PagedResponse;
import com.example.clinic.service.api.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.clinic.service.api.ApiPaths.DOCTORS;
import static com.example.clinic.service.core.security.SecurityConstants.HAS_ADMIN;
import static com.example.clinic.service.core.security.SecurityConstants.HAS_PATIENT;

@RestController
@RequestMapping(DOCTORS)
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize(HAS_ADMIN + " or " + HAS_PATIENT)
    public ResponseEntity<PagedResponse<DoctorResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(doctorService.getAllDoctors(page));
    }

    @PostMapping
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        doctorService.createDoctor(request);
        return ResponseEntity.ok(new MessageResponse("Доктор успешно создан"));
    }

    @GetMapping("/{doctorId}")
    @PreAuthorize(HAS_ADMIN + " or " + HAS_PATIENT)
    public ResponseEntity<DoctorDetailResponse> getDoctorDetail(
            @PathVariable Long doctorId
    ) {
        return ResponseEntity.ok(doctorService.getDetailDoctor(doctorId));
    }

    @PatchMapping("/{doctorId}")
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<MessageResponse> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody PatchDoctorRequest request
    ) {
        doctorService.updateDoctor(request, doctorId);
        return ResponseEntity.ok(new MessageResponse("Данные успешно обновлены"));
    }
}
