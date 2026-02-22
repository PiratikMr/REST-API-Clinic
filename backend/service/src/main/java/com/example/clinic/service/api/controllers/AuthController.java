package com.example.clinic.service.api.controllers;

import com.example.clinic.service.api.exceptions.ResourceNotFoundException;
import com.example.clinic.service.core.repositories.DoctorRepository;
import com.example.clinic.service.core.repositories.PatientRepository;
import com.example.clinic.service.core.repositories.UserRepository;
import com.example.clinic.service.core.security.jwt.JwtUtils;
import com.example.clinic.service.core.security.user.UserDetailsImpl;
import com.example.clinic.service.api.dto.request.LoginRequest;
import com.example.clinic.service.api.dto.request.SignupRequest;
import com.example.clinic.service.api.dto.response.JwtResponse;
import com.example.clinic.service.api.dto.response.MessageResponse;
import com.example.clinic.service.entities.Patient;
import com.example.clinic.service.entities.User;
import com.example.clinic.service.entities.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.clinic.service.api.ApiPaths.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final DoctorRepository doctorRepository;

    @PostMapping(LOGIN)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        Long entityId = -1L;
        if (role != null) {

            String roleString = role.split("_")[1].toLowerCase().trim();

            entityId = switch (roleString) {
                case "admin" -> userDetails.getId();
                case "patient" -> patientRepository.findByUserUserId(userDetails.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Пациент не найден с ID: " + userDetails.getId())).getPatientId();
                case "doctor" -> doctorRepository.findByUserUserId(userDetails.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Доктор не найден с ID: " + userDetails.getId())).getDoctorId();
                default -> entityId;
            };
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                entityId,
                userDetails.getUsername(),
                role));
    }

    @PostMapping(REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Имя пользователя уже занято"));
        }

        User user = new User();
        user.setUsername(signUpRequest.getLogin());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.PATIENT);

        User savedUser = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setFirstName(signUpRequest.getFirstName());
        patient.setLastName(signUpRequest.getLastName());
        patient.setMiddleName(signUpRequest.getMiddleName());
        patient.setAddress(signUpRequest.getAddress());

        patientRepository.save(patient);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован"));
    }

}
