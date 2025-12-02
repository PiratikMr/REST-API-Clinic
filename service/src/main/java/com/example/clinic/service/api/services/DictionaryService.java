package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.CreateDictionaryRequest;
import com.example.clinic.service.api.dto.request.Patch.PatchDictionaryRequest;
import com.example.clinic.service.api.dto.response.PagedResponse;
import com.example.clinic.service.api.dto.response.objects.DictionaryResponse;
import com.example.clinic.service.api.services.enums.DictionaryType;
import com.example.clinic.service.core.repositories.MedicationRepository;
import com.example.clinic.service.core.repositories.ProcedureRepository;
import com.example.clinic.service.core.repositories.SpecialtyRepository;
import com.example.clinic.service.core.repositories.TestRepository;
import com.example.clinic.service.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.clinic.service.api.services.ServiceConfig.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final SpecialtyRepository specialtyRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final TestRepository testRepository;

    public List<DictionaryResponse> getAllDictionary(
            DictionaryType type
    ) {
        return (switch (type) {
            case SPECIALTY -> specialtyRepository.findAllByOrderByNameAsc();
            case MEDICATION -> medicationRepository.findAllByOrderByNameAsc();
            case PROCEDURE -> procedureRepository.findAllByOrderByNameAsc();
            case TEST -> testRepository.findAllByOrderByNameAsc();
        }).stream().map(entity -> convertToDto(type, entity)).toList();
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
                specialtyRepository.deleteById(id);
                break;
            }
            case MEDICATION: {
                medicationRepository.deleteById(id);
                break;
            }
            case PROCEDURE: {
                procedureRepository.deleteById(id);
                break;
            }
            case TEST: {
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
                Specialty specialty = specialtyRepository.findById(id).orElseThrow();
                specialty.setName(name);
                specialtyRepository.save(specialty);
            }
            case MEDICATION: {
                Medication medication = medicationRepository.findById(id).orElseThrow();
                medication.setName(name);
                medicationRepository.save(medication);
            }
            case PROCEDURE: {
                Procedure procedure = procedureRepository.findById(id).orElseThrow();
                procedure.setName(name);
                procedureRepository.save(procedure);
            }
            case TEST: {
                Test test = testRepository.findById(id).orElseThrow();
                test.setName(name);
                testRepository.save(test);
            }
        }
    }


    private <T> DictionaryResponse convertToDto(DictionaryType type, T entity) {
        return switch(type) {
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
