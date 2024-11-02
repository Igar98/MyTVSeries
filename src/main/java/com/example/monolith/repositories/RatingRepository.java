package com.example.monolith.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.monolith.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    public Optional<Rating> findBySerieIdAndUserId(UUID serieId, UUID userId);

    public Page<Rating> findBySerieId(UUID serieId, Pageable pageable);

    public Page<Rating> findByUserId(UUID userId, Pageable pageable);
}
