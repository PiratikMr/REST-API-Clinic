package com.example.clinic.service.api.services;

import com.example.clinic.service.api.dto.request.CreateDoctorRequest;
import com.example.clinic.service.api.dto.request.Patch.PatchDoctorRequest;
import com.example.clinic.service.api.dto.request.objects.ScheduleDtoRequest;
import com.example.clinic.service.api.dto.response.DoctorDetailResponse;
import com.example.clinic.service.api.dto.response.DoctorResponse;
import com.example.clinic.service.api.dto.response.PagedResponse;
import com.example.clinic.service.api.dto.response.objects.ScheduleDtoResponse;
import com.example.clinic.service.core.repositories.*;
import com.example.clinic.service.entities.*;
import com.example.clinic.service.entities.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.clinic.service.api.services.ServiceConfig.MINUTES_FOR_APPOINTMENT;
import static com.example.clinic.service.api.services.ServiceConfig.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorSlotRepository doctorSlotRepository;
    private final PlaceRepository placeRepository;
    private final PasswordEncoder passwordEncoder;

    public PagedResponse<DoctorResponse> getAllDoctors(int page, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<Doctor> doctorsPage = doctorRepository.findAll(pageable);

        return new PagedResponse<>(doctorsPage.map(DoctorResponse::new));
    }

    @Transactional
    public void createDoctor(CreateDoctorRequest request) {
        if (userRepository.existsByUsername(request.getLogin())) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new RuntimeException("Specialty not found"));

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setMiddleName(request.getMiddleName());
        doctor.setSpecialty(specialty);
        Doctor savedDoctor = doctorRepository.save(doctor);

        if (request.getSchedules() != null) {
            addSchedules(request.getSchedules(), savedDoctor);
        }
    }


    public DoctorDetailResponse getDetailDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Доктор не найден!"));

        List<DoctorSlot> slots = doctorSlotRepository.findAvailableDoctorSlotsByDoctor(doctor, LocalDateTime.now().plusMonths(1));

        List<ScheduleDtoResponse> schedules = new ArrayList<>();
        for (DoctorSlot slot : slots) {
            ScheduleDtoResponse dto = new ScheduleDtoResponse();

            dto.setId(slot.getSlotId());
            dto.setRoom(slot.getPlace().getRoom());
            dto.setDistrict(slot.getPlace().getDistrict());
            dto.setStartTime(slot.getStartTime());
            dto.setEndTime(slot.getEndTime());

            schedules.add(dto);
        }

        DoctorDetailResponse dto = new DoctorDetailResponse(doctor);
        dto.setSchedules(schedules);
        return dto;
    }


    @Transactional
    public void updateDoctor(PatchDoctorRequest request, Long doctorId) {

        Doctor doctor =  doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Доктор не найден!"));

        if (request.getFirstName() != null) {
            doctor.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            doctor.setLastName(request.getLastName());
        }

        if (request.getMiddleName() != null) {
            doctor.setMiddleName(request.getMiddleName());
        }

        if (request.getSpecialtyId() != null) {
            Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                    .orElseThrow(() -> new RuntimeException("Специальности не существует!"));
            doctor.setSpecialty(specialty);
        }

        if (request.getSchedules() != null) {
            addSchedules(request.getSchedules(), doctor);
        }

        doctorRepository.save(doctor);
    }


    private void addSchedules(List<ScheduleDtoRequest> schedules, Doctor savedDoctor) {
        for (ScheduleDtoRequest schedDto : schedules) {

            Place place = new Place();
            place.setRoom(schedDto.getRoom());
            place.setDistrict(schedDto.getDistrict());
            Place savedPlace = placeRepository.save(place);

            LocalDateTime currTime = schedDto.getFrom();
            LocalDateTime endTime = schedDto.getTo();

            while (currTime.isBefore(endTime)) {
                DoctorSlot slot = new DoctorSlot();
                slot.setDoctor(savedDoctor);
                slot.setPlace(savedPlace);
                slot.setStartTime(currTime);
                currTime = currTime.plusMinutes(MINUTES_FOR_APPOINTMENT);
                slot.setEndTime(currTime);
                doctorSlotRepository.save(slot);
            }
        }
    }
}
