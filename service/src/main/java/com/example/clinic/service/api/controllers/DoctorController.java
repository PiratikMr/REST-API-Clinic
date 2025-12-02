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

@RestController
@RequestMapping(DOCTORS)
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<PagedResponse<DoctorResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(doctorService.getAllDoctors(page, sortBy, sortDir));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        try {
            doctorService.createDoctor(request);
            return ResponseEntity.ok(new MessageResponse("Доктор успешно создан"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<DoctorDetailResponse> getDoctorDetail(
            @PathVariable Long doctorId
    ) {
        try {
            return ResponseEntity.ok(doctorService.getDetailDoctor(doctorId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody PatchDoctorRequest request
    ) {
        try {
            doctorService.updateDoctor(request, doctorId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

//    @DeleteMapping("/{doctorId}" + SCHEDULES + "/{scheduleId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> getDoctor(
//            @PathVariable Long doctorId,
//            @PathVariable Long scheduleId
//    ) {
//        try {
//            doctorService.deleteSchedule(doctorId, scheduleId);
//            return ResponseEntity.ok(new MessageResponse("Schedule deleted successfully"));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
//        }
//    }

}
