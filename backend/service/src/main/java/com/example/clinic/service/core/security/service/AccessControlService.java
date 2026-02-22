package com.example.clinic.service.core.security.service;

import com.example.clinic.service.api.exceptions.ResourceNotFoundException;
import com.example.clinic.service.core.repositories.AppointmentRepository;
import com.example.clinic.service.core.repositories.DoctorRepository;
import com.example.clinic.service.core.repositories.PatientRepository;
import com.example.clinic.service.core.security.user.UserDetailsImpl;
import com.example.clinic.service.entities.Doctor;
import com.example.clinic.service.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("accessControl")
@RequiredArgsConstructor
public class AccessControlService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public boolean isPatientOwner(Long patientId, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Пациент не найден с ID: " + patientId));

        if (!patient.getUser().getUserId().equals(principal.getId())) {
            throw new AccessDeniedException("Вы пытаетесь получить доступ к данным другого пациента");
        }

        return true;
    }

    public boolean isDoctorOwner(Long doctorId, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Доктор не найден с ID: " + doctorId));

        if (!doctor.getUser().getUserId().equals(principal.getId())) {
            throw new AccessDeniedException("Вы пытаетесь получить доступ к данным другого доктора");
        }

        return true;
    }

    public boolean isAppointmentOwner(Long appointmentId, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> appointment.getPatient().getUser().getUserId().equals(principal.getId()))
                .orElse(false);
    }

    public boolean isAppointmentDoctor(Long appointmentId, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> appointment.getDoctorSlot().getDoctor().getUser().getUserId().equals(principal.getId()))
                .orElse(false);
    }
}
