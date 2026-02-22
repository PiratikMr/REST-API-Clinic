package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.CreateDictionaryRequest;
import com.example.clinic.service.api.dto.request.Patch.PatchDictionaryRequest;
import com.example.clinic.service.api.dto.response.objects.DictionaryResponse;
import com.example.clinic.service.api.exceptions.ResourceNotFoundException;
import com.example.clinic.service.api.services.enums.DictionaryType;
import com.example.clinic.service.core.repositories.MedicationRepository;
import com.example.clinic.service.core.repositories.ProcedureRepository;
import com.example.clinic.service.core.repositories.SpecialtyRepository;
import com.example.clinic.service.core.repositories.TestRepository;
import com.example.clinic.service.entities.*;
import lombok.RequiredArgsConstructor;
import com.example.clinic.service.api.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final SpecialtyRepository specialtyRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final TestRepository testRepository;

    public PageResponse<DictionaryResponse> getAllDictionary(
            DictionaryType type, String search, Pageable pageable) {
        Page<?> pageResult = switch (type) {
            case SPECIALTY -> (search == null || search.isBlank()) ? specialtyRepository.findAll(pageable)
                    : specialtyRepository.findByNameContainingIgnoreCase(search, pageable);
            case MEDICATION -> (search == null || search.isBlank()) ? medicationRepository.findAll(pageable)
                    : medicationRepository.findByNameContainingIgnoreCase(search, pageable);
            case PROCEDURE -> (search == null || search.isBlank()) ? procedureRepository.findAll(pageable)
                    : procedureRepository.findByNameContainingIgnoreCase(search, pageable);
            case TEST -> (search == null || search.isBlank()) ? testRepository.findAll(pageable)
                    : testRepository.findByNameContainingIgnoreCase(search, pageable);
        };

        Page<DictionaryResponse> dtoPage = pageResult.map(entity -> convertToDto(type, entity));
        return new PageResponse<>(dtoPage, search);
    }

    public void createDictionaryItem(DictionaryType type, CreateDictionaryRequest request) {
        switch (type) {
            case SPECIALTY: {
                Specialty specialty = new Specialty();
                specialty.setName(request.getName());
                specialtyRepository.save(specialty);
                break;
            }
            case MEDICATION: {
                Medication medication = new Medication();
                medication.setName(request.getName());
                medicationRepository.save(medication);
                break;
            }
            case PROCEDURE: {
                Procedure procedure = new Procedure();
                procedure.setName(request.getName());
                procedureRepository.save(procedure);
                break;
            }
            case TEST: {
                Test test = new Test();
                test.setName(request.getName());
                testRepository.save(test);
                break;
            }
        }
    }

    public void deleteDictionaryItem(DictionaryType type, Long id) {
        switch (type) {
            case SPECIALTY: {
                if (!specialtyRepository.existsById(id)) {
                    throw new ResourceNotFoundException("Специальность не найдена с ID: " + id);
                }
                specialtyRepository.deleteById(id);
                break;
            }
            case MEDICATION: {
                if (!medicationRepository.existsById(id)) {
                    throw new ResourceNotFoundException("Лекарство не найдено с ID: " + id);
                }
                medicationRepository.deleteById(id);
                break;
            }
            case PROCEDURE: {
                if (!procedureRepository.existsById(id)) {
                    throw new ResourceNotFoundException("Процедура не найдена с ID: " + id);
                }
                procedureRepository.deleteById(id);
                break;
            }
            case TEST: {
                if (!testRepository.existsById(id)) {
                    throw new ResourceNotFoundException("Анализ не найден с ID: " + id);
                }
                testRepository.deleteById(id);
                break;
            }
        }
    }

    public void updateDictionaryItem(DictionaryType type, PatchDictionaryRequest request) {
        Long id = request.getId();
        String name = request.getName();

        switch (type) {
            case SPECIALTY: {
                Specialty specialty = specialtyRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Специальность не найдена с ID: " + id));
                specialty.setName(name);
                specialtyRepository.save(specialty);
            }
            case MEDICATION: {
                Medication medication = medicationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Лекарство не найдено с ID: " + id));
                medication.setName(name);
                medicationRepository.save(medication);
            }
            case PROCEDURE: {
                Procedure procedure = procedureRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Процедура не найдена с ID: " + id));
                procedure.setName(name);
                procedureRepository.save(procedure);
            }
            case TEST: {
                Test test = testRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Анализ не найден с ID: " + id));
                test.setName(name);
                testRepository.save(test);
            }
        }
    }

    private <T> DictionaryResponse convertToDto(DictionaryType type, T entity) {
        return switch (type) {
            case SPECIALTY -> convertToDto((Specialty) entity);
            case MEDICATION -> convertToDto((Medication) entity);
            case PROCEDURE -> convertToDto((Procedure) entity);
            case TEST -> convertToDto((Test) entity);
        };
    }

    private DictionaryResponse convertToDto(Specialty entity) {
        DictionaryResponse dto = new DictionaryResponse();
        dto.setId(entity.getSpecialtyId());
        dto.setName(entity.getName());
        return dto;
    }

    private DictionaryResponse convertToDto(Medication entity) {
        DictionaryResponse dto = new DictionaryResponse();
        dto.setId(entity.getMedicationId());
        dto.setName(entity.getName());
        return dto;
    }

    private DictionaryResponse convertToDto(Procedure entity) {
        DictionaryResponse dto = new DictionaryResponse();
        dto.setId(entity.getProcedureId());
        dto.setName(entity.getName());
        return dto;
    }

    private DictionaryResponse convertToDto(Test entity) {
        DictionaryResponse dto = new DictionaryResponse();
        dto.setId(entity.getTestId());
        dto.setName(entity.getName());
        return dto;
    }
}
