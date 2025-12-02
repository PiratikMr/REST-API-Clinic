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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.clinic.service.api.ApiPaths.*;

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
    public ResponseEntity<?> createSpecialty(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.SPECIALTY, request);
    }

    @PatchMapping(SPECIALTIES)
    public ResponseEntity<?> updateSpecialty(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.SPECIALTY, request);
    }

    @DeleteMapping(SPECIALTIES + "/{specialtyId}")
    public ResponseEntity<?> deleteSpecialty(@PathVariable Long specialtyId) {
        return processDelete(DictionaryType.SPECIALTY, specialtyId);
    }


    @GetMapping(MEDICATIONS)
    public ResponseEntity<List<DictionaryResponse>> getAllMedications() {
        return processGetAll(DictionaryType.MEDICATION);
    }

    @PostMapping(MEDICATIONS)
    public ResponseEntity<?> createMedication(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.MEDICATION, request);
    }

    @PatchMapping(MEDICATIONS)
    public ResponseEntity<?> updateMedication(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.MEDICATION, request);
    }

    @DeleteMapping(MEDICATIONS + "/{medicationId}")
    public ResponseEntity<?> deleteMedication(@PathVariable Long medicationId) {
        return processDelete(DictionaryType.MEDICATION, medicationId);
    }


    @GetMapping(PROCEDURES)
    public ResponseEntity<List<DictionaryResponse>> getAllProcedures() {
        return processGetAll(DictionaryType.PROCEDURE);
    }

    @PostMapping(PROCEDURES)
    public ResponseEntity<?> createProcedure(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.PROCEDURE, request);
    }

    @PatchMapping(PROCEDURES)
    public ResponseEntity<?> updateProcedure(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.PROCEDURE, request);
    }

    @DeleteMapping(PROCEDURES + "/{procedureId}")
    public ResponseEntity<?> deleteProcedure(@PathVariable Long procedureId) {
        return processDelete(DictionaryType.PROCEDURE, procedureId);
    }


    @GetMapping(TESTS)
    public ResponseEntity<List<DictionaryResponse>> getAllTests() {
        return processGetAll(DictionaryType.TEST);
    }

    @PostMapping(TESTS)
    public ResponseEntity<?> createTest(@Valid @RequestBody CreateDictionaryRequest request) {
        return processCreate(DictionaryType.TEST, request);
    }

    @PatchMapping(TESTS)
    public ResponseEntity<?> updateTest(@Valid @RequestBody PatchDictionaryRequest request) {
        return processUpdate(DictionaryType.TEST, request);
    }

    @DeleteMapping(TESTS + "/{testId}")
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