package com.example.monolith.services.interfaces;

import java.util.UUID;

import com.example.monolith.domain.AppUser;

public interface AppUserService {

    @Internal("Used by other services to get the user entity directly")
    AppUser getUserEntityById(UUID id);
    
}
