package com.example.monolith.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.monolith.domain.AppUser;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.repositories.UserRepository;
import com.example.monolith.services.interfaces.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;

    public AppUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser getUserEntityById(UUID id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }
}
