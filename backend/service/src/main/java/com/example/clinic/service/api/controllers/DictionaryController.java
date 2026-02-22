package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.dto.request.CreateDictionaryRequest;
import com.example.clinic.service.api.dto.request.Patch.PatchDictionaryRequest;
import com.example.clinic.service.api.dto.response.objects.DictionaryResponse;
import com.example.clinic.service.api.services.DictionaryService;
import com.example.clinic.service.api.services.enums.DictionaryType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.clinic.service.api.ApiPaths.*;
import static com.example.clinic.service.core.security.SecurityConstants.HAS_ADMIN;

@RestController
@RequestMapping(DICTIONARIES)
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;


    @GetMapping(SPECIALTIES)
    public ResponseEntity<List<DictionaryResponse>> getAllSpecialties() {
        return processGetAll(DictionaryType.SPECIALTY);
    }

    @PostMapping(SPECIALTIES)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> createSpecialty(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.SPECIALTY, request);
    }

    @PatchMapping(SPECIALTIES)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> updateSpecialty(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.SPECIALTY, request);
    }

    @DeleteMapping(SPECIALTIES + "/{specialtyId}")
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> deleteSpecialty(@PathVariable Long specialtyId) {
        return processDelete(DictionaryType.SPECIALTY, specialtyId);
    }


    @GetMapping(MEDICATIONS)
    public ResponseEntity<List<DictionaryResponse>> getAllMedications() {
        return processGetAll(DictionaryType.MEDICATION);
    }

    @PostMapping(MEDICATIONS)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> createMedication(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.MEDICATION, request);
    }

    @PatchMapping(MEDICATIONS)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> updateMedication(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.MEDICATION, request);
    }

    @DeleteMapping(MEDICATIONS + "/{medicationId}")
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> deleteMedication(@PathVariable Long medicationId) {
        return processDelete(DictionaryType.MEDICATION, medicationId);
    }


    @GetMapping(PROCEDURES)
    public ResponseEntity<List<DictionaryResponse>> getAllProcedures() {
        return processGetAll(DictionaryType.PROCEDURE);
    }

    @PostMapping(PROCEDURES)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> createProcedure(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.PROCEDURE, request);
    }

    @PatchMapping(PROCEDURES)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> updateProcedure(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.PROCEDURE, request);
    }

    @DeleteMapping(PROCEDURES + "/{procedureId}")
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> deleteProcedure(@PathVariable Long procedureId) {
        return processDelete(DictionaryType.PROCEDURE, procedureId);
    }


    @GetMapping(TESTS)
    public ResponseEntity<List<DictionaryResponse>> getAllTests() {
        return processGetAll(DictionaryType.TEST);
    }

    @PostMapping(TESTS)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> createTest(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.TEST, request);
    }

    @PatchMapping(TESTS)
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> updateTest(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.TEST, request);
    }

    @DeleteMapping(TESTS + "/{testId}")
    @PreAuthorize(HAS_ADMIN)
    public ResponseEntity<?> deleteTest(@PathVariable Long testId) {
        return processDelete(DictionaryType.TEST, testId);
    }




    private ResponseEntity<List<DictionaryResponse>> processGetAll(DictionaryType type) {
        return ResponseEntity.ok(dictionaryService.getAllDictionary(type));
    }

    private ResponseEntity<?> processCreate(DictionaryType type, CreateDictionaryRequest request) {
        dictionaryService.createDictionaryItem(type, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity<?> processUpdate(DictionaryType type, PatchDictionaryRequest request) {
        dictionaryService.updateDictionaryItem(type, request);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> processDelete(DictionaryType type, Long id) {
        dictionaryService.deleteDictionaryItem(type, id);
        return ResponseEntity.ok().build();
    }
}