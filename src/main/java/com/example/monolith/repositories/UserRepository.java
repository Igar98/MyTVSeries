package com.example.monolith.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.monolith.domain.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    
    
}
