package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.Patch.PatchPatientRequest;
import com.example.clinic.service.api.dto.response.PatientResponse;
import com.example.clinic.service.core.repositories.PatientRepository;
import com.example.clinic.service.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).get();
        return new PatientResponse(patient);
    }

    public void updatePatient(Long patientId, PatchPatientRequest request) {
        Patient patient = patientRepository.findById(patientId).get();

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

}
