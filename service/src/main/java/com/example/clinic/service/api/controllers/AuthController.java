package com.example.clinic.service.api.controllers;

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

    @PostMapping(LOGIN)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Аутентификация через Spring Security (проверяет логин/пароль)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        // Установка аутентификации в контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерация токена
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Получение данных пользователя для ответа
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                role));
    }

    // 2. РЕГИСТРАЦИЯ ПАЦИЕНТА
    @PostMapping(REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Проверка: занят ли логин
        if (userRepository.existsByUsername(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // 1. Создаем User
        User user = new User();
        user.setUsername(signUpRequest.getLogin());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Обязательно хешируем пароль!
        user.setRole(Role.PATIENT); // Регистрация доступна только пациентам

        User savedUser = userRepository.save(user);

        // 2. Создаем профиль Patient (обязательно, так как в БД constraints)
        Patient patient = new Patient();
        patient.setUser(savedUser); // Связываем с созданным юзером
        patient.setFirstName(signUpRequest.getFirstName());
        patient.setLastName(signUpRequest.getLastName());
        patient.setMiddleName(signUpRequest.getMiddleName());
        patient.setAddress(signUpRequest.getAddress());

        patientRepository.save(patient);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
