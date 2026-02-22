package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.Patch.PatchPatientRequest;
import com.example.clinic.service.api.dto.response.PatientResponse;
import com.example.clinic.service.api.exceptions.ResourceNotFoundException;
import com.example.clinic.service.core.repositories.PatientRepository;
import com.example.clinic.service.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse getPatientById(Long patientId) {
        return new PatientResponse(getPatientEntity(patientId));
    }

    public void updatePatient(Long patientId, PatchPatientRequest request) {
        Patient patient = getPatientEntity(patientId);

        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }

        if (request.getMiddleName() != null) {
            patient.setMiddleName(request.getMiddleName());
        }

        if (request.getMiddleName() != null) {
            patient.setMiddleName(request.getMiddleName());
        }

        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }

        patientRepository.save(patient);
    }


    private Patient getPatientEntity(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Пациент не найден с ID: " + patientId));
    }
}
