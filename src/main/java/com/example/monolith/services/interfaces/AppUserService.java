package com.example.monolith.services.interfaces;

import java.util.UUID;

import com.example.monolith.domain.AppUser;

public interface AppUserService {

    /**
     * Get a user by its id. For internal use only.
     * 
     * @param id User id.
     * @return User entity.
     */
    @Internal("Used by other services to get the user entity directly")
    AppUser getUserEntityById(UUID id);

    /**
     * Check if a user exists by its id.
     * 
     * @param id User id.
     * @return True if the user exists, false otherwise.
     */
    public boolean existsById(UUID id);
    
}
