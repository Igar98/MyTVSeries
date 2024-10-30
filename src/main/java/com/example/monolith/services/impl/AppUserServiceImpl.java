package com.example.monolith.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.monolith.domain.AppUser;
import com.example.monolith.repositories.UserRepository;
import com.example.monolith.services.interfaces.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;

    public AppUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser getUserEntityById(UUID id) {
        //TODO: Check if return null or ResourceNotFoundException
        return userRepository.findById(id).orElse(null);
    }
    
}
