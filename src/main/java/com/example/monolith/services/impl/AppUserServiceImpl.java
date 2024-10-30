package com.example.monolith.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.monolith.domain.AppUser;
import com.example.monolith.repositories.UserRepository;
import com.example.monolith.services.interfaces.AppUserService;
import com.example.monolith.web.mappers.AppUserMapper;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;
    private final AppUserMapper appUserMapper;

    public AppUserServiceImpl(UserRepository userRepository, AppUserMapper appUserMapper) {
        this.userRepository = userRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public AppUser getUserEntityById(UUID id) {
        //TODO: Check if return null or ResourceNotFoundException
        return userRepository.findById(id).orElse(null);
    }
    
}
