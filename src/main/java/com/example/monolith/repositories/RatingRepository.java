package com.example.monolith.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.monolith.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    
}
