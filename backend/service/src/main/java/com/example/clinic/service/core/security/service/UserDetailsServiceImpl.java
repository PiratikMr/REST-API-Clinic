package com.example.clinic.service.core.security.service;

import com.example.clinic.service.api.exceptions.ResourceNotFoundException;
import com.example.clinic.service.core.repositories.UserRepository;
import com.example.clinic.service.core.security.user.UserDetailsImpl;
import com.example.clinic.service.entities.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким логином не найден"));

        return UserDetailsImpl.build(user);
    }
}
